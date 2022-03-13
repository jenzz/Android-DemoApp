package com.jenzz.demo.users

import android.os.Parcel
import android.os.Parcelable
import com.jenzz.demo.common.ToastMessage
import okhttp3.HttpUrl

/*
 * Alternatively, a sealed class with multiple implementations can be used for the view state.
 * Since we want to continue showing the loading spinner while the cached data is displayed,
 * we are using a data class for simplicity instead.
 */
data class UsersViewState(
    val isLoading: Boolean,
    val users: List<User>,
    val toastMessage: ToastMessage? = null,
)

data class User(
    val id: UserId,
    val name: UserName,
    val nickname: UserNickname,
    val email: UserEmail,
    val website: HttpUrl,
    val phone: PhoneNumber,
)

@JvmInline
value class UserId(val value: Int) : Parcelable {

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(value)
    }

    companion object CREATOR : Parcelable.Creator<UserId> {

        override fun createFromParcel(parcel: Parcel): UserId = UserId(parcel.readInt())

        override fun newArray(size: Int): Array<UserId?> = arrayOfNulls(size)
    }
}

@JvmInline
value class UserName(val value: String)

@JvmInline
value class UserNickname(val value: String)

@JvmInline
value class UserEmail(val value: String)

@JvmInline
value class PhoneNumber(val value: String)
