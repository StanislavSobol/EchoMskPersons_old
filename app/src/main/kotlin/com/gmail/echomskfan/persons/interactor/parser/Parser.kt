package com.gmail.echomskfan.persons.interactor.parser

import android.content.Context
import android.support.annotation.VisibleForTesting
import com.gmail.echomskfan.persons.data.ItemDTO
import com.gmail.echomskfan.persons.data.PersonDTO
import com.gmail.echomskfan.persons.data.TextDTO
import com.gmail.echomskfan.persons.data.entity.VipEntity
import com.gmail.echomskfan.persons.utils.ThrowableManager
import org.json.JSONArray
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.io.IOException
import java.nio.charset.Charset
import java.util.*


class Parser : IParser {

    override fun getVips(context: Context): List<VipEntity> {
        val result = mutableListOf<VipEntity>()

        val jsonString = loadJSONFromAsset(context, "vips.json")
        val jsonArray = JSONArray(jsonString)

        val size = jsonArray.length()

        for (i in 0 until size) {
            val jsonObject = jsonArray.getJSONObject(i)
            result.add(
                    VipEntity(
                            jsonObject.get("url") as String,
                            jsonObject.get("firstName") as String,
                            jsonObject.get("lastName") as String,
                            jsonObject.get("profession") as String,
                            jsonObject.get("info") as String,
                            jsonObject.get("photoUrl") as String
                    )
            )
        }

        return result
    }


    override fun getCasts(url: String, vipEntity: VipEntity): List<ItemDTO> {
        if (url.isEmpty()) return listOf()

        val document: Document?
        try {
            try {
                document = getDocument(url)
            } catch (e: IllegalArgumentException) {
                ThrowableManager.process(e)
                return listOf()
            }

        } catch (e: IOException) {
            ThrowableManager.process(e)
            return listOf()
        }

        return document?.let { parseItems(document, vipEntity) } ?: listOf()
    }

    @Throws(IOException::class)
    private fun getDocument(url: String): Document? {
        return Jsoup.connect(url).get()
    }

    @Synchronized
    private fun parseItems(document: Document, vipEntity: VipEntity): List<ItemDTO> {
        val result = ArrayList<ItemDTO>()
        val divs = document.getElementsByTag("div")
        for (div in divs) {
            val divsPrevcontent = div.getElementsByClass("prevcontent")
            for (divPrevcontent in divsPrevcontent) {


                var person = ""
                try {
                    person = divPrevcontent.getElementsByTag("p")[0]
                            .getElementsByTag("a")[0]
                            .getElementsByClass("about")[0]
                            .getElementsByTag("strong").text()
                } catch (e: IndexOutOfBoundsException) {
                    // Logger.writeError("parseItems()::wrong person");
                }

                if (!person.contains(vipEntity.lastName)) {
                    continue
                }

                var fullTextURL = ""
                var type = ""
                var subtype = ""
                var photoURL = ""
                var shortText = ""
                var mp3Url = ""
                var mp3Duration = 0
                var formattedDate = ""

                try {

                    type = divPrevcontent.getElementsByClass("section")[0]
                            .getElementsByTag("a")[0]
                            .getElementsByTag("strong")[0].text()

                } catch (e: IndexOutOfBoundsException) {
                    //                    Logger.writeError("parseItems()::item.type");
                }

                try {
                    subtype = divPrevcontent.getElementsByClass("section")[0]
                            .getElementsByTag("a")[1]
                            .getElementsByTag("span")[0].text()

                } catch (e: IndexOutOfBoundsException) {
                    //                    Logger.writeError("parseItems()::item.subtype ");
                }

                try {
                    photoURL = divPrevcontent.getElementsByTag("p")[0]
                            .getElementsByTag("a")[0]
                            .getElementsByClass("photo")[0]
                            .getElementsByTag("img")[0].attr("src")

                } catch (e: IndexOutOfBoundsException) {
                    //                    Logger.writeError("parseItems()::item.photoURL");
                }

                try {
                    shortText = divPrevcontent.getElementsByTag("p")[1]
                            .getElementsByTag("a")[0].text()
                } catch (e: IndexOutOfBoundsException) {
                    //                    Logger.writeError("parseItems()::item.shortText");
                }

                if (shortText.isEmpty()) {
                    try {
                        shortText = divPrevcontent.getElementsByClass("txt")[0].text()
                    } catch (e: IndexOutOfBoundsException) {
                        //                        Logger.writeError("parseItems()::item.shortText 2");
                    }

                }

                try {
                    mp3Url = divPrevcontent.getElementsByClass("mediamenu")[0]
                            .getElementsByTag("a")[3].attr("href")
                } catch (e: IndexOutOfBoundsException) {
                    //                    Logger.writeError("parseItems()::item.mp3URL");
                }

                if (mp3Url.isEmpty()) {
                    try {
                        mp3Url = divPrevcontent.getElementsByClass("mediamenu")[0]
                                .getElementsByTag("a")[2].attr("href")
                    } catch (e: IndexOutOfBoundsException) {
                        //                        Logger.writeError("parseItems()::item.mp3URL 2");
                    }

                }

                try {
                    val myMp3Duration = divPrevcontent.getElementsByClass("mediamenu")[0]
                            .getElementsByTag("a")[0]
                            .getElementsByTag("span")[2].text()
                    mp3Duration = Integer.valueOf(myMp3Duration.substring(0, 2)) * 60 + Integer.valueOf(myMp3Duration.substring(3, 5))

                } catch (e: IndexOutOfBoundsException) {
                    //                    Logger.writeError("parseItems()::item.mp3Duration");
                }


                try {
                    fullTextURL = divPrevcontent.getElementsByClass("meta")[0]
                            .getElementsByTag("a")[2].attr("data-url")

                } catch (e: IndexOutOfBoundsException) {
                    //                    Logger.writeError("parseItems()::item.fullTextURL");
                }

                try {
                    formattedDate = divPrevcontent.getElementsByClass("datetime")[0].attr("title")
                } catch (e: IndexOutOfBoundsException) {
                    //                    Logger.writeError("parseItems()::item.formattedDate");
                }


                val item = ItemDTO(fullTextURL,
                        type,
                        subtype,
                        photoURL,
                        shortText,
                        mp3Url,
                        mp3Duration,
                        formattedDate
                )

                if (!result.contains(item)) {
                    if (!item.fullTextURL.isEmpty() || !item.mp3URL.isEmpty()) {
                        result.add(item)
                    }
                }
            }
        }
        return result
    }

    @VisibleForTesting
    fun loadJSONFromAsset(context: Context, assetName: String): String {
        val json: String
        val `is` = context.assets.open(assetName)
        val size = `is`.available()
        val buffer = ByteArray(size)
        `is`.read(buffer)
        `is`.close()
        json = String(buffer, Charset.forName("UTF-8"))
        return json
    }

     override fun getTextData(url: String): TextDTO? {
        var result: TextDTO? = null

        getDocument(url)?.let {
            try {
                result = parseText(it)
            } catch (e: IndexOutOfBoundsException) {

            }
        }

        return result
    }

    @Throws(IndexOutOfBoundsException::class)
    private fun parseText(document: Document): TextDTO {
        val textDTO = TextDTO()


        val divs = document.getElementsByTag("div")
        try {
            setManyPersons(textDTO, divs)
        } catch (e: IndexOutOfBoundsException) {
            setOnePerson(textDTO, divs)
        }

        if (textDTO.personDTOs.isEmpty()) {
            // from Latynina (one person)
            setOnePerson(textDTO, divs)
        }

        for (div in divs) {
            if (div.className() == "typical dialog _ga1_on_ contextualizable include-relap-widget" ||
                    div.className() ==     "typical  _ga1_on_ contextualizable include-relap-widget") {
                val all = div.allElements
                for (each in all) {
                    if (each.getElementsByTag("div").size > 0) {
                        continue
                    }

                    var author = ""
                    val pS = each.getElementsByTag("p")
                    if (pS.size > 0) {
                        val p = each.getElementsByTag("p")[0]
                        val bS = p.getElementsByTag("b")
                        if (bS.size > 0) {
                            val b = p.getElementsByTag("b")[0]
                            author = b.text().trim { it <= ' ' }
                            val text = p.text().replace(author, "").replace("―", "").trim { it <= ' ' }
                            textDTO.addTextWithAuthor(text, author)
                        } else {
                            val text = pS.text().replace(author, "").replace("―", "").trim { it <= ' ' }
                            textDTO.addTextWithAuthor(text, author)
                        }
                    }

                    val blockquoteS = each.getElementsByTag("blockquote")
                    if (blockquoteS.size > 0) {
                        val blockquote = blockquoteS[0]
                        val text = blockquote.text().trim { it <= ' ' }
                        textDTO.addQuote(text)
                    }
                }
            }
        }

        return textDTO
    }

    private fun setManyPersons(textDTO: TextDTO, divs: Elements) {
        for (div in divs) {
            if (div.className() == "conthead discuss") {
                var tmp: Element = div.getElementsByTag("div")[6]
                tmp = tmp.getElementsByTag("div")[2]
                val presenters = tmp.getElementsByTag("a")

                for (a in presenters) {
                    val person = PersonDTO(name = a.text())
                    textDTO.personDTOs.add(person)
                }
                break
            }
        }
    }

    private fun setOnePerson(textDTO: TextDTO, divs: Elements) {
        for (div in divs) {
            if (div.className() == "conthead discuss") {
                val presenter = div.getElementsByTag("div")[6]
                        .getElementsByTag("div")[0]
                        .getElementsByTag("a")[0]
                        .getElementsByTag("span")[1]

                val person = PersonDTO(
                        name = presenter.getElementsByClass("name").text() + " - " + presenter.getElementsByClass("post").text()
                )
                textDTO.personDTOs.add(person)
                break
            }
        }
    }
}
