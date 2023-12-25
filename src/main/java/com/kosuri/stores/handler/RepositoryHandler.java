package com.kosuri.stores.handler;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.kosuri.stores.constant.StoreConstants;
import com.kosuri.stores.dao.*;
import com.kosuri.stores.model.enums.UserType;
import com.kosuri.stores.model.request.*;
import com.kosuri.stores.model.request.OTPRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kosuri.stores.exception.APIException;

import jakarta.validation.Valid;
import org.springframework.util.ObjectUtils;

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
	private DiagnosticServiceRepository diagnosticServiceRepository;

	@Autowired
	private DiagnosticMasterRepository diagnosticMasterRepository;


	@Autowired
	private OtpHandler otpHandler;
	@Autowired
	private PrimaryCareCenterRepoistory primaryCareCenterRepoistory;


	public StoreEntity addStoreToRepository(@Valid StoreEntity storeEntity) throws Exception {
		Optional<StoreEntity> store = storeRepository.findById(storeEntity.getId());
		Optional<UserOTPEntity> userOTPEntityOptional = userOTPRepository.findByUserEmailAndActive(storeEntity.getOwnerEmail(),1);
		if (store.isPresent()) {
			throw new APIException("Store with this id is already present");
		}else if (userOTPEntityOptional.isEmpty()){
			throw new APIException("Store User Not Found ");
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

//		if (!role.isPresent()) {
//			throw new APIException("Role does not exist. Please enter a valid role");
//		}
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
			OTPRequest otpRequest = createOTPRequest(storeEntity.getStoreUserEmail(),storeEntity.getStoreUserContact());
			boolean isMessageSent = sendEmailOtp(otpRequest);
			boolean isPhoneOtpSent = sendOtpToSMS(otpRequest);
			return true;
		}
		return false;
    }

	private OTPRequest createOTPRequest(String storeUserEmail, String storeUserContact) {
		OTPRequest otpRequest = new OTPRequest();
		otpRequest.setEmail(storeUserEmail);
		otpRequest.setPhoneNumber(storeUserContact);
		return otpRequest;
	}


	public TabStoreUserEntity loginUser(LoginUserRequest request) throws Exception {

		Optional<TabStoreUserEntity> tabStoreUserEntityOptional = tabStoreRepository.findByStoreUserEmailOrStoreUserContact(request.getEmail(),
				request.getPhoneNumber());
		if (tabStoreUserEntityOptional.isEmpty()) {
			throw new APIException("Invalid Credentials!");
		}
		TabStoreUserEntity tabStoreUserEntity = tabStoreUserEntityOptional.orElse(null);
		if (tabStoreUserEntity.getPassword() != null && isValidPassword(request.getPassword(), tabStoreUserEntity.getPassword())) {
			System.out.println("User logged in successfully");
			return tabStoreUserEntity;
		}

		throw new APIException("Invalid Credentials!");
	}

	private boolean isValidPassword(String password, String encryptedPassword) {
		BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), encryptedPassword);
		return result.verified;
	}

	public boolean validateStoreUser(AddTabStoreUserRequest request) throws Exception {
		Optional<TabStoreUserEntity> tabStoreUserEntityOptional = tabStoreRepository
				.findByStoreUserEmailOrStoreUserContact(request.getUserEmail(), request.getUserPhoneNumber());
		if (tabStoreUserEntityOptional.isEmpty()) {
			return true;
		}
		TabStoreUserEntity tabStoreUserEntity = tabStoreUserEntityOptional.orElse(null);
			if (tabStoreUserEntity.getStoreUserEmail().contains(request.getUserEmail())) {
				System.out.println("User already exists in system");
				throw new APIException("User already exists in system");
			}
		return true;
	}

	public boolean verifyEmailOtp(VerifyOTPRequest verifyOTPRequest) {
		String email = verifyOTPRequest.getEmail();
		String emailOtp = verifyOTPRequest.getOtp();
		UserOTPEntity userOtpEntity = userOTPRepository.findByUserEmailAndEmailOtpOrForgetEmailOtp(email,emailOtp);

			if (userOtpEntity != null) {
				if (verifyOTPRequest.getIsForgetPassword()) {
                    return userOtpEntity.getActive() != null
                            && userOtpEntity.getActive() == 1;
				} else {
					StoreConstants.IS_EMAIL_ALREADY_VERIFIED = userOtpEntity.isEmailVerify();
					if (!userOtpEntity.isEmailVerify()) {
						userOtpEntity.setEmailVerify(true);
						userOtpEntity.setActive(1);
						userOtpEntity.setUpdatedOn(LocalTime.now().toString());
						userOTPRepository.save(userOtpEntity);
						return true;
					}
				}
			}
		return false;
	}

	public boolean verifyPhoneOtp(VerifyOTPRequest verifyOTPRequest) {
		String phoneOtp = verifyOTPRequest.getOtp();
		String phoneNumber = verifyOTPRequest.getPhoneNumber();
		UserOTPEntity userOtpEntity = userOTPRepository.findByUserPhoneNumberAndPhoneOtpOrForgetEmailOtp(phoneNumber,phoneOtp);

		if (userOtpEntity != null) {
			if (verifyOTPRequest.getIsForgetPassword()) {
				return userOtpEntity.getActive() != null
						&& userOtpEntity.getActive() == 1;
			} else{
				userOtpEntity.setSmsVerify(true);
				userOtpEntity.setActive(1);
				userOtpEntity.setUpdatedOn(LocalTime.now().toString());
				userOTPRepository.save(userOtpEntity);
				return true;
			}
		}
		return false;
	}

	public boolean sendEmailOtp(@Valid OTPRequest request) {
		Optional<TabStoreUserEntity> tabStoreUserOptional = tabStoreRepository.findByStoreUserEmail(request.getEmail());
		TabStoreUserEntity tabStoreUserEntity = tabStoreUserOptional.orElse(null);
		if (null != tabStoreUserEntity &&
				null != tabStoreUserEntity.getUserType() &&
				tabStoreUserEntity.getUserType().equalsIgnoreCase(UserType.SA.toString())) {
			String storeUserEmail = request.getEmail();
			return (request.getIsForgetPassword() ? otpHandler.sendOtpToEmail(storeUserEmail, true):
					otpHandler.sendOtpToEmail(storeUserEmail, false));
		}
		return false;
	}

	public boolean sendOtpToSMS(@Valid OTPRequest request) {
		Optional<TabStoreUserEntity> tabStoreUserOptional = tabStoreRepository.findByStoreUserContact(request.getPhoneNumber());
		TabStoreUserEntity tabStoreUserEntity = tabStoreUserOptional.orElse(null);
		if (null != tabStoreUserEntity && null != tabStoreUserEntity.getUserType() &&
				tabStoreUserEntity.getUserType().equalsIgnoreCase(UserType.SA.toString())) {
			String storeUserPhoneNumber = request.getPhoneNumber();
			return otpHandler.sendOtpToPhoneNumber(storeUserPhoneNumber);
		}
		return false;
	}


	public void addDiagnosticCenter(DiagnosticServicesEntity diagnosticServicesEntity, DiagnosticCenterRequest request) {
		diagnosticServiceRepository.save(diagnosticServicesEntity);
	}

	public boolean isDCActive(DiagnosticCenterRequest request) {
		Optional<StoreEntity> storeInfoOptional = storeRepository.findById(request.getStoreId());
		StoreEntity storeEntity = storeInfoOptional.orElse(null);
		return (Objects.requireNonNull(storeEntity).getStatus().equalsIgnoreCase("true"));

	}

	public DiagnosticServicesEntity findServiceById(String userServiceId) {
		Optional<DiagnosticServicesEntity> diagnosticServicesEntityOptional = diagnosticServiceRepository.findById(userServiceId);
        return diagnosticServicesEntityOptional.orElse(null);
	}

	public void saveDiagnosticServiceEntity(DiagnosticServicesEntity serviceEntity) {
		diagnosticServiceRepository.save(serviceEntity);
	}

	public boolean isPCActive(PrimaryCareUserRequest request) {
		Optional<StoreEntity> storeInfoOptional = storeRepository.findById(request.getStoreId());
		StoreEntity storeEntity = storeInfoOptional.orElse(null);
		return (Objects.requireNonNull(storeEntity).getStatus().equalsIgnoreCase("true"));

	}

	public void addPrimaryCareCenter(PrimaryCareEntity primaryCareEntity, PrimaryCareUserRequest request) {
		primaryCareCenterRepoistory.save(primaryCareEntity);

	}

	public PrimaryCareEntity findPrimaryServiceById(String userServiceId) {
		Optional<PrimaryCareEntity> primaryCareEntityOptional = primaryCareCenterRepoistory.findById(userServiceId);
		return primaryCareEntityOptional.orElse(null);
	}

	public void savePrimaryServiceEntity(PrimaryCareEntity serviceEntity) {
		primaryCareCenterRepoistory.save(serviceEntity);
	}

	public boolean updatePassword(TabStoreUserEntity tabStoreUserEntity) {
		TabStoreUserEntity tabStoreUserResponse = tabStoreRepository.save(tabStoreUserEntity);
		return (!ObjectUtils.isEmpty(tabStoreUserResponse));
	}

	public TabStoreUserEntity getTabStoreUser(String emailAddress, String userContactNumber) {
		Optional<TabStoreUserEntity> tabStoreUserEntityOptional = tabStoreRepository.findByStoreUserEmailOrStoreUserContact(emailAddress,userContactNumber);
		return tabStoreUserEntityOptional.orElse(null);
	}
}