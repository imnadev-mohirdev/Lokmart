package uz.mohirdev.lokmart.data.store

import uz.mohirdev.lokmart.data.api.auth.dto.UserDto
import javax.inject.Inject

class UserStore @Inject constructor() : BaseStore<UserDto>("user", UserDto::class.java)