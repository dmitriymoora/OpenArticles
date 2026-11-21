package com.example.openarticles.data.remote

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive

// DOAJ отдаёт publisher то строкой, то объектом {"name","country"}
internal object DoajPublisherAsStringSerializer : KSerializer<String> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("DoajPublisherAsString", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): String {
        val jsonDecoder = decoder as? JsonDecoder ?: return decoder.decodeString()
        return when (val element = jsonDecoder.decodeJsonElement()) {
            is JsonPrimitive -> element.contentOrNull.orEmpty()
            is JsonObject -> element["name"]?.jsonPrimitive?.contentOrNull.orEmpty()
            else -> ""
        }
    }

    override fun serialize(encoder: Encoder, value: String) {
        val jsonEncoder = encoder as? JsonEncoder
        if (jsonEncoder != null) {
            jsonEncoder.encodeJsonElement(JsonPrimitive(value))
        } else {
            encoder.encodeString(value)
        }
    }
}

// Ответ сервера при поиске статей (содержит пагинацию и список результатов)
@Serializable
internal data class DoajSearchResponse(
    val total: Int = 0,
    val page: Int = 1,
    val pageSize: Int = 0,
    val results: List<DoajArticleResult> = emptyList(),
)

// Элемент списка результатов поиска статей (хранит ID и метаданные)
@Serializable
internal data class DoajArticleResult(
    val id: String,
    val bibjson: DoajArticleBibjson = DoajArticleBibjson(),
)

// Библиографические метаданные конкретной научной статьи
@Serializable
internal data class DoajArticleBibjson(
    val title: String = "",
    @SerialName("abstract")
    val abstractText: String = "",
    val author: List<DoajAuthor> = emptyList(),
    val keywords: List<String> = emptyList(),
    val year: String? = null,
    val month: String? = null,
    val journal: DoajJournalRef = DoajJournalRef(),
    val identifier: List<DoajIdentifier> = emptyList(),
    val link: List<DoajLink> = emptyList(),
)

// Информация об авторе научной статьи
@Serializable
internal data class DoajAuthor(
    val name: String = "",
)

// Краткие данные о журнале, в котором опубликована статья
@Serializable
internal data class DoajJournalRef(
    val title: String = "",
    @Serializable(with = DoajPublisherAsStringSerializer::class)
    val publisher: String = "",
    val issns: List<String> = emptyList(),
)

// Идентификатор публикации или журнала (например, DOI, eISSN, pISSN)
@Serializable
internal data class DoajIdentifier(
    val id: String = "",
    val type: String = "",
)

// Веб-ссылка, связанная со статьей (например, на полный текст или PDF)
@Serializable
internal data class DoajLink(
    val url: String = "",
    val type: String = "",
    @SerialName("content_type")
    val contentType: String = "",
)

// Ответ сервера при поиске журналов (содержит общее количество и список результатов)
@Serializable
internal data class DoajJournalSearchResponse(
    val total: Int = 0,
    val results: List<DoajJournalResult> = emptyList(),
)

// Элемент списка результатов поиска журналов (хранит ID и метаданные)
@Serializable
internal data class DoajJournalResult(
    val id: String,
    val bibjson: DoajJournalBibjson = DoajJournalBibjson(),
)

// Подробные библиографические метаданные самого научного журнала
@Serializable
internal data class DoajJournalBibjson(
    val title: String = "",
    @Serializable(with = DoajPublisherAsStringSerializer::class)
    val publisher: String = "",
    val eissn: String? = null,
    val pissn: String? = null,
    val keywords: List<String> = emptyList(),
    val language: List<String> = emptyList(),
)
