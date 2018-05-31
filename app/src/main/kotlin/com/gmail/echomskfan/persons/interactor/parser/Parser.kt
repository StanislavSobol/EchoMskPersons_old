package com.gmail.echomskfan.persons.interactor.parser

import android.content.Context
import android.support.annotation.VisibleForTesting
import com.gmail.echomskfan.persons.data.ItemDTO
import com.gmail.echomskfan.persons.data.VipDTO
import com.gmail.echomskfan.persons.utils.ThrowableManager
import org.json.JSONArray
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException
import java.nio.charset.Charset
import java.util.*


class Parser : IParser {

    override fun getVips(context:Context): List<VipDTO> {
        val result = mutableListOf<VipDTO>()

        val jsonString = loadJSONFromAsset(context,"vips.json")
        val jsonArray = JSONArray(jsonString)
      //  val jsonArray = json.getJSONArray("")

        val size = jsonArray.length()

        for (i in 0 until size) {
            val jsonObject = jsonArray.getJSONObject(i)
            result.add(
                    VipDTO(
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

    override fun getVip(url: String): VipDTO? {
        return null
        /*
        if (url.isEmpty()) {
            return null
        }

        val result: VipDTO

        val document: Document
        try {
            try {
                document = getDocument(url)
            } catch (e: IllegalArgumentException) {
                ThrowableManager.process(e)
                return null
            }

            val divProfile = document.body()
                    .getElementsByTag("div")[0]
                    .getElementsByClass("content")[0]
                    .getElementsByClass("column")[0]
                    .getElementsByClass("profile")[0]


            val personDiv = divProfile
                    .getElementsByTag("div")[2]

            result = VipDTO()

            val name = personDiv.getElementsByTag("h1")[0].text()
            result.setFirstName(StringUtils.getFirstName(name))
            result.setLastName(StringUtils.getLastName(name))

            result.setUrl(url)
            result.setProfession(personDiv.getElementsByTag("span")[0].text())
            result.setInfo(personDiv.getElementsByTag("div")[0].text())

            val photoDiv = divProfile
                    .getElementsByTag("div")[1]
                    .getElementsByTag("img")[0]

            result.setPhotoUrl(photoDiv.attr("src"))

        } catch (e: IOException) {
            ThrowableManager.process(e)
            return null
        }

        return result
        */
    }


    override fun getCasts(url: String, vipDTO: VipDTO): List<ItemDTO> {
        if (url.isEmpty()) return listOf()

        val document: Document
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

        return parseItems(document, vipDTO)
    }

    @Throws(IOException::class)
    private fun getDocument(url: String): Document {
        return Jsoup.connect(url).get()
    }

    @Synchronized
    private fun parseItems(document: Document, vipDTO: VipDTO): List<ItemDTO> {
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

                if (!person.contains(vipDTO.lastName)) {
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
                var pageNum = 0
                var orderWithinPage = 0
                var favorite = false

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
                        formattedDate,
                        pageNum,
                        orderWithinPage,
                        favorite)

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
}
