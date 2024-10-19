package com.example.sahara

import kotlinx.serialization.Serializable

@Serializable
data class Wound(
    val message: String
)

@Serializable
data class InjuryInfo(
    val category: String,
    val treatment: List<String>,
    val severity: String,
    val symptoms: List<String>,
    val complications: List<String>,
    val medications: List<String>,
    val prevention: List<String>,
    val recovery_time: RecoveryTime,
    val recommended_action: List<String>,
    val alternative_remedies: List<String>? = null
)

@Serializable
data class RecoveryTime(
    val min_days: String,
    val max_days: String
)

@Serializable
data class FoodResponse(
    val additives: List<String>?,
    val allergens: List<String>?,
    val brand: String?,
    val description: String?,
    val certifications: List<String>?,
    val diabetic_friendly: String?,
    val dietary_restrictions: List<String>?,
    val health_ratings: HealthRatings?,
    val ingredients: List<String>?,
    val keto_friendly: String?,
    val name: String?,
    val nutritional_info: NutritionalInfo?,
    val origin_country: String?,
    val vegan_friendly: String?
)

@Serializable
data class HealthRatings(
    val overall: String?,
    val sustainability_score: String?
)

@Serializable
data class Macronutrients(
    val carbohydrates: String?,
    val fat: String?,
    val protein: String?
)

@Serializable
data class Micronutrients(
    val calcium: String?,
    val vitamin_d: String?,
    val vitamin_e: String?
)

@Serializable
data class NutritionalInfo(
    val calories: String?,
    val macronutrients: Macronutrients?,
    val micronutrients: Micronutrients?,
    val serving_size: String?
)

@Serializable
data class ObjectResponse(
    val detected_objects: List<String>,
    val response: Response
)

@Serializable
data class DetailedProcess(
    val materials_needed: String,
    val instructions: String,
    val tips: String
)

@Serializable
data class Response(
    val type: String,
    val project_name: String,
    val objects_that_can_be_used: String,
    val time_taken: String,
    val detailed_process: DetailedProcess,
    val time_require: String
)