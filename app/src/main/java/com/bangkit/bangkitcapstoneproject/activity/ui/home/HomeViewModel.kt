package com.bangkit.bangkitcapstoneproject.activity.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.bangkitcapstoneproject.R
import com.bangkit.bangkitcapstoneproject.activity.network.ApiConfig
import com.bangkit.bangkitcapstoneproject.activity.network.ApiResponse
import com.bangkit.bangkitcapstoneproject.activity.network.Result
import com.bangkit.bangkitcapstoneproject.activity.utils.reduceFileImage
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class HomeViewModel : ViewModel() {

    private val apiService = ApiConfig().getApiService()
    var imgUri: String = ""
    var path: String = ""
    var file: File? = null
    var isResponseBodyNullOrError = false
    var isResponseSuccessful = false
    lateinit var result: Result

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData("")
    val message: LiveData<String> = _message

    fun showMessage(string: String) {
        _message.value = string
    }

    fun showLoading(boolean: Boolean) {
        _isLoading.value = boolean
    }

    fun getResult() {
        if (file != null) {
            showLoading(true)
            val rotatedBitmap = reduceFileImage(file as File)
            val imageFile = rotatedBitmap.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                rotatedBitmap.name,
                imageFile
            )

            result = Result(
                "Jamur Tiram",
                "Pleurotus ostreatus",
                "Dapat Dikonsumsi"
            )
            showLoading(false)
            isResponseBodyNullOrError = false
            isResponseSuccessful = true

//            Karena Api Belum Siap kita anggap saja sudah Succesful dulu
//            val client = apiService.getResult(imageMultipart)
//            client.enqueue(object : Callback<ApiResponse> {
//                override fun onResponse(
//                    call: Call<ApiResponse>,
//                    response: Response<ApiResponse>
//                ) {
//                    if (response.isSuccessful) {
//                        val responseBody = response.body()
//                        if (responseBody != null && !responseBody.error) {
//                            result = responseBody.result
//                            showLoading(false)
//                            isResponseBodyNullOrError = false
//                            isResponseSuccessful = true
//                        } else {
//                            showLoading(false)
//                            isResponseBodyNullOrError = true
//                            isResponseSuccessful = true
//                            responseBody?.message?.let { showMessage(it) }
//                        }
//                    } else {
//                        showLoading(false)
//                        isResponseSuccessful = false
//                        showMessage(response.message())
//                    }
//                }
//
//                override fun onFailure(
//                    call: Call<ApiResponse>,
//                    t: Throwable
//                ) {
//                    showLoading(false)
//                    showMessage(R.string.onFailure.toString())
//                }
//            })
        }
    }
}