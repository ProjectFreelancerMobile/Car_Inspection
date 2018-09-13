package com.car_inspection.data.model.inspectiondetails

data class Inspector(
        val id: Int,
        val name: String,
        val fullname: String,
        val email: String,
        val status: Int,
        val created_at: String,
        val updated_at: String
)