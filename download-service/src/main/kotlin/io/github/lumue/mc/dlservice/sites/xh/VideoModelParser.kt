package io.github.lumue.mc.dlservice.sites.xh
import com.fasterxml.jackson.databind.ObjectMapper
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class VideoModelParser{

    val objectMapper : ObjectMapper= ObjectMapper()

    fun fromHtml(htmlAsString: String):VideoModel{
        val doc= Jsoup.parse(htmlAsString)
        return objectMapper.readValue(doc.initialsJsonString,PageInitials::class.java).videoModel
    }




    val Document.initialsJsonString: String
    get():String{
        val initialsScriptString=run {
            //2. Parses and scrapes the HTML response
            select("#initials-script").first().dataNodes().first().wholeData
        }
        val startOfInitialsJson = initialsScriptString.indexOfFirst { c -> '{' == c }
        val initialsJsonString = initialsScriptString.substring(startOfInitialsJson)
        return initialsJsonString
    }

}
