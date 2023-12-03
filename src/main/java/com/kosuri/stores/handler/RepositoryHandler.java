package com.kosuri.stores.handler;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import com.kosuri.stores.constant.StoreConstants;
import com.kosuri.stores.model.enums.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kosuri.stores.dao.PurchaseEntity;
import com.kosuri.stores.dao.PurchaseRepository;
import com.kosuri.stores.dao.RoleEntity;
import com.kosuri.stores.dao.RoleRepository;
import com.kosuri.stores.dao.SaleEntity;
import com.kosuri.stores.dao.SaleRepository;
import com.kosuri.stores.dao.StockEntity;
import com.kosuri.stores.dao.StockRepository;
import com.kosuri.stores.dao.StoreEntity;
import com.kosuri.stores.dao.StoreRepository;
import com.kosuri.stores.dao.TabStoreRepository;
import com.kosuri.stores.dao.TabStoreUserEntity;
import com.kosuri.stores.dao.UserOTPEntity;
import com.kosuri.stores.dao.UserOTPRepository;
import com.kosuri.stores.exception.APIException;
import com.kosuri.stores.model.request.AddTabStoreUserRequest;
import com.kosuri.stores.model.request.AddUserRequest;
import com.kosuri.stores.model.request.LoginUserRequest;

import jakarta.validation.Valid;

@Service
public class RepositoryHandler {

	@Autowired
	private StoreRepository storeRepository;
	@Autowired
	private PurchaseRepository purchaseRepository;

	@Autowired
	private SaleRepository saleRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private StockRepository stockRepository;

	@Autowired
	private TabStoreRepository tabStoreRepository;

	@Autowired
	private UserOTPRepository userOTPRepository;

	@Autowired
	private OtpHandler otpHandler;


	public StoreEntity addStoreToRepository(@Valid StoreEntity storeEntity) throws Exception {
		Optional<StoreEntity> store = storeRepository.findById(storeEntity.getId());
		boolean storeExistsByOwnerEmail = storeRepository.existsByOwnerEmail(storeEntity.getOwnerEmail());
		if (store.isPresent()) {
			throw new APIException("Store with this id is already present");
		}
		return storeRepository.save(storeEntity);

	}

	public StoreEntity updateStore(@Valid StoreEntity storeEntity) throws Exception {
		Optional<StoreEntity> store = storeRepository.findById(storeEntity.getId());

		if (store.isEmpty()) {
			System.out.println("Entity not found");
			throw new APIException("Store with this id not found!");
		}

		return storeRepository.save(storeEntity);
	}

	public void addUser(@Valid StoreEntity storeEntity, AddUserRequest request) throws Exception {
		// TODO Update to query based on id
		Optional<RoleEntity> role = roleRepository.findByRoleName(request.getRole());
		if (!role.isPresent()) {
			throw new APIException("Role does not exist. Please enter a valid role");
		}

		storeRepository.save(storeEntity);

	}

	public List<StoreEntity> getAllStores() throws Exception {
		List<StoreEntity> storeEntities = storeRepository.findAll();
		return storeEntities;
	}

	public Optional<List<PurchaseEntity>> getPurchaseRecordsByStore(String storeId) {
		return purchaseRepository.findByStoreId(storeId);
	}

	public Optional<List<SaleEntity>> getSaleRecordsByStore(String storeId) {
		return saleRepository.findByStoreId(storeId);
	}

	public Optional<List<StockEntity>> getStockRecordsByStore(String storeId) {
		return stockRepository.findByStoreId(storeId);
	}

	public boolean validateuser(AddUserRequest request) throws Exception {
		Optional<List<StoreEntity>> existingStores = storeRepository.findByOwnerEmailOrOwnerContact(request.getEmail(),
				request.getPhoneNumber());
		if (existingStores.isEmpty()) {
			return true;
		}
		for (StoreEntity store : existingStores.get()) {
			// TODO Update to query based on id
			if (store.getId().contains("DUMMY")) {
				System.out.println("User already exists in system");
				throw new APIException("User already exists in system");
			}
		}

		return true;
	}

	public boolean addStoreUser(@Valid TabStoreUserEntity storeEntity, AddTabStoreUserRequest request) throws Exception {
		// TODO Update to query based on id
		Optional<RoleEntity> role = roleRepository.findByRoleName(request.getRole());

		if (!role.isPresent()) {
			throw new APIException("Role does not exist. Please enter a valid role");
		}
		TabStoreUserEntity user = tabStoreRepository.save(storeEntity);
		if ( user.getStoreUserEmail() != null) {
			//sets the EmailId and created Date for UserOTPEnitity
			UserOTPEntity userOtp = new UserOTPEntity();
			userOtp.setUserOtpId(storeEntity.getUserId());
			userOtp.setUserEmail(request.getUserEmail());
			userOtp.setActive(0);
			userOtp.setCreatedOn(LocalDateTime.now().toString());
			userOtp.setUserPhoneNumber(storeEntity.getStoreUserContact());
			userOTPRepository.save(userOtp);
			boolean isMessageSent = sendEmailOtp(storeEntity);
			boolean isPhoneOtpSent = sendOtpToSMS(storeEntity);
			return true;
		}
		return false;
    }


	public StoreEntity loginUser(LoginUserRequest request) throws Exception {
		Optional<List<StoreEntity>> existingStores = storeRepository.findByOwnerEmailOrOwnerContact(request.getEmail(),
				request.getPhoneNumber());
		if (existingStores.isEmpty()) {
			throw new APIException("Invalid Credentials!");
		}

		for (StoreEntity store : existingStores.get()) {
			// TODO Update to query based on id
			if (store.getPassword() != null && store.getPassword().equals(request.getPassword())
					&& store.getId().contains("DUMMY")) {
				System.out.println("User logged in successfully");
				return store;
			}
		}

		throw new APIException("Invalid Credentials!");
	}

	public boolean validateStoreUser(AddTabStoreUserRequest request) throws Exception {
		Optional<List<TabStoreUserEntity>> existingStores = tabStoreRepository
				.findByStoreUserEmailOrStoreUserContact(request.getUserEmail(), request.getUserPhoneNumber());
		if (existingStores.isEmpty()) {
			return true;
		}
		for (TabStoreUserEntity store : existingStores.get()) {
			// TODO Update to query based on id
			if (store.getStoreUserEmail().contains(request.getUserEmail())) {
				System.out.println("User already exists in system");
				throw new APIException("User already exists in system");
			}
		}

		return true;
	}

	public boolean verifyEmailOtp(@Valid String email, @Valid String emailOtp) {
		Optional<UserOTPEntity> userOtpEntityOptional = userOTPRepository.findByUserEmailAndEmailOtp(email,emailOtp);
		UserOTPEntity userOtpEntity = userOtpEntityOptional.orElse(null);

		if (userOtpEntity != null) {
			StoreConstants.IS_EMAIL_ALREADY_VERIFIED = userOtpEntity.isEmailVerify();
			if (!userOtpEntity.isEmailVerify()){
				userOtpEntity.setEmailVerify(true);
				userOtpEntity.setActive(1);
				userOtpEntity.setUpdatedOn(LocalTime.now().toString());
				userOTPRepository.save(userOtpEntity);
				return true;
			}
		} else {
			return false;
		}
		return false;
	}

	public boolean verifyPhoneOtp(@Valid String phoneOtp, @Valid String phoneNumber) {
		Optional<UserOTPEntity> userOtpEntityOptional = userOTPRepository.findByUserPhoneNumberAndPhoneOtp(phoneNumber,phoneOtp);
		UserOTPEntity userOtpEntity = userOtpEntityOptional.orElse(null);
		if (userOtpEntity != null) {
			userOtpEntity.setSmsVerify(true);
			userOtpEntity.setActive(1);
			userOtpEntity.setUpdatedOn(LocalTime.now().toString());
			userOTPRepository.save(userOtpEntity);
			return true;
		} else {
			return false;
		}
	}

	public boolean sendEmailOtp(@Valid TabStoreUserEntity request) {
		Optional<TabStoreUserEntity> tabStoreUserOptional = tabStoreRepository.findById(request.getUserId());
		TabStoreUserEntity tabStoreUserEntity = tabStoreUserOptional.orElse(null);
		if (null != tabStoreUserEntity && tabStoreUserEntity.getUserType().equalsIgnoreCase(UserType.SA.toString())) {
			String storeUserEmail = request.getStoreUserEmail();
			return otpHandler.sendOtpToEmail(storeUserEmail);
		}
		return false;
	}

	public boolean sendOtpToSMS(@Valid TabStoreUserEntity request) {
		Optional<TabStoreUserEntity> tabStoreUserOptional = tabStoreRepository.findById(request.getUserId());
		TabStoreUserEntity tabStoreUserEntity = tabStoreUserOptional.orElse(null);
		if (null != tabStoreUserEntity && tabStoreUserEntity.getUserType().equalsIgnoreCase(UserType.SA.toString())) {
			String storeUserPhoneNumber = request.getStoreUserContact();
			return otpHandler.sendOtpToPhoneNumber(storeUserPhoneNumber);
		}
		return false;
	}


}