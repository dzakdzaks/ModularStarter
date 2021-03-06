package id.medigo.auth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import id.medigo.auth.domain.GetLoginUseCase
import id.medigo.auth.fragment.LoginFragmentDirections
import id.medigo.common.base.BaseViewModel
import id.medigo.common.extension.ValidatorType
import id.medigo.common.extension.validate
import id.medigo.common.utils.ErrorHandler
import id.medigo.common.utils.Event
import id.medigo.model.Profile
import id.medigo.repository.PreferenceRepository
import id.medigo.repository.utils.AppDispatchers
import id.medigo.repository.utils.Resource
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(
    private val getLoginUseCase: GetLoginUseCase,
    private val preferenceRepository: PreferenceRepository,
    private val dispatchers: AppDispatchers
): BaseViewModel(preferenceRepository, dispatchers) {

    val username: MutableLiveData<String> = MutableLiveData()
    val usernameErrorMessage: MutableLiveData<String> = MutableLiveData()
    val isUsernameValid: MutableLiveData<Boolean> = MutableLiveData()

    val password: MutableLiveData<String> = MutableLiveData()
    val passwordErrorMessage: MutableLiveData<String> = MutableLiveData()
    val isPasswordValid: MutableLiveData<Boolean> = MutableLiveData()

    fun registerClicked() =
        navigateTo(LoginFragmentDirections.actionLoginFragmentToRegisterFeature())


    private val _profile = MediatorLiveData<Resource<Profile>>()
    val profile: LiveData<Resource<Profile>> get() = _profile

    private var profileSource: LiveData<Resource<Profile>> = MutableLiveData<Resource<Profile>>()

    fun loginClicked(username: String?, password: String?) {
        username.validate(ValidatorType.USER_NAME).apply {
            usernameErrorMessage.value = message
            isUsernameValid.value = isValid
        }
        password.validate(ValidatorType.PASSWORD).apply {
            isPasswordValid.value = isValid
            passwordErrorMessage.value = message
        }
        if (isUsernameValid.value == true && isPasswordValid.value == true) login(username, password)
    }

    private fun login(username: String?, password: String?) = viewModelScope.launch(dispatchers.main) {
        _profile.postValue(null)
        _profile.removeSource(profileSource)
        withContext(dispatchers.io) { profileSource = getLoginUseCase(username?: "", password?: "") }
        _profile.addSource(profileSource) { resource ->
            _profile.postValue(resource)
            if (resource.status == Resource.Status.SUCCESS) {
                onLogginSuccess()
            }
            if (resource.status == Resource.Status.ERROR) {
                _errorHandler.postValue(Event(
                    ErrorHandler(
                        ErrorHandler.ErrorType.SNACKBAR,
                        resource.error
                    )
                ))
            }
        }
    }

    private fun onLogginSuccess()
            = navigateTo(LoginFragmentDirections.actionPopOutAuthFeature())

}