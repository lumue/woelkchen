package io.github.lumue.mc.dlservice.sites.xh


import org.junit.Assert
import org.junit.Test



class VideoModelParserTest {


    val jsonString="{\n" +
            "\t\t\t\t\"duration\": 275,\n" +
            "\t\t\t\t\"title\": \"Nina Elle's big fat bombs in your face! - Naughty America\",\n" +
            "\t\t\t\t\"pageURL\": \"https:\\/\\/de.xhamster.com\\/videos\\/nina-elle-s-big-fat-bombs-in-your-face-naughty-america-8653702\",\n" +
            "\t\t\t\t\"icon\": null,\n" +
            "\t\t\t\t\"spriteURL\": \"https:\\/\\/thumb-v2.xhcdn.com\\/a\\/exEARJHel80J4yCristdog\\/008\\/653\\/702\\/160x160.50.s.jpg\",\n" +
            "\t\t\t\t\"trailerURL\": \"https:\\/\\/thumb-v2.xhcdn.com\\/a\\/XrQ_BNEY-T64qIWTqaJpQw\\/008\\/653\\/702\\/240x135.t.mp4\",\n" +
            "\t\t\t\t\"rating\": {\n" +
            "\t\t\t\t\t\"modelName\": \"ratingModel\",\n" +
            "\t\t\t\t\t\"value\": 98,\n" +
            "\t\t\t\t\t\"entityModel\": \"videoModel\",\n" +
            "\t\t\t\t\t\"entityID\": 8653702,\n" +
            "\t\t\t\t\t\"likes\": 56,\n" +
            "\t\t\t\t\t\"dislikes\": 1,\n" +
            "\t\t\t\t\t\"state\": 0\n" +
            "\t\t\t\t},\n" +
            "\t\t\t\t\"isVR\": false,\n" +
            "\t\t\t\t\"isHD\": true,\n" +
            "\t\t\t\t\"isUHD\": false,\n" +
            "\t\t\t\t\"created\": 1512564002,\n" +
            "\t\t\t\t\"modelName\": \"videoModel\",\n" +
            "\t\t\t\t\"thumbURL\": \"https:\\/\\/thumb-v-cl2.xhcdn.com\\/a\\/2HwiMvEEBePzYrgSRBLl9g\\/008\\/653\\/702\\/1280x720.4.jpg\",\n" +
            "\t\t\t\t\"id\": 8653702,\n" +
            "\t\t\t\t\"views\": 57360,\n" +
            "\t\t\t\t\"comments\": 2,\n" +
            "\t\t\t\t\"modified\": 0,\n" +
            "\t\t\t\t\"orientation\": 0,\n" +
            "\t\t\t\t\"secured\": 0,\n" +
            "\t\t\t\t\"status\": 2,\n" +
            "\t\t\t\t\"description\": \"Nina Elle&#039;s hot, but her sister&#039;s not! She&#039;s trying to get her man&#039;s friend Sean to go on a date with her sis, but he&#039;s not having it. UNTIL Nina sticks her big tits in his face! Game on!\",\n" +
            "\t\t\t\t\"mp4File\": \"https:\\/\\/19-10.b.cdn13.com\\/008\\/653\\/702\\/240p.h264.mp4?cdn_creation_time=1536843600&cdn_ttl=14400&cdn_bw=118k&cdn_bw_fs=312k&cdn_cv_data=77.180.197.184-dvp&cdn_hash=e5ef9d46c5dbe941ec9528e35ee70a28\",\n" +
            "\t\t\t\t\"spriteCount\": 50,\n" +
            "\t\t\t\t\"playerThumbURL\": \"https:\\/\\/static-cl.xhcdn.com\\/images\\/showcase.png\",\n" +
            "\t\t\t\t\"sources\": {\n" +
            "\t\t\t\t\t\"download\": {\n" +
            "\t\t\t\t\t\t\"240p\": {\n" +
            "\t\t\t\t\t\t\t\"link\": \"https:\\/\\/de.xhamster.com\\/movies\\/8653702\\/download\\/240p\",\n" +
            "\t\t\t\t\t\t\t\"size\": 15.96\n" +
            "\t\t\t\t\t\t},\n" +
            "\t\t\t\t\t\t\"480p\": {\"link\": \"https:\\/\\/de.xhamster.com\\/movies\\/8653702\\/download\\/480p\", \"size\": 31.91},\n" +
            "\t\t\t\t\t\t\"720p\": {\"link\": \"https:\\/\\/de.xhamster.com\\/movies\\/8653702\\/download\\/720p\", \"size\": 60.14}\n" +
            "\t\t\t\t\t},\n" +
            "\t\t\t\t\t\"mp4\": {\n" +
            "\t\t\t\t\t\t\"720p\": \"https:\\/\\/19-10.b.cdn13.com\\/008\\/653\\/702\\/720p.h264.mp4?cdn_creation_time=1536843600&cdn_ttl=14400&cdn_bw=448k&cdn_bw_fs=312k&cdn_cv_data=77.180.197.184-dvp&cdn_hash=96ca7aaad459d81f0b01303b54ad20c2\",\n" +
            "\t\t\t\t\t\t\"480p\": \"https:\\/\\/19-10.b.cdn13.com\\/008\\/653\\/702\\/480p.h264.mp4?cdn_creation_time=1536843600&cdn_ttl=14400&cdn_bw=238k&cdn_bw_fs=312k&cdn_cv_data=77.180.197.184-dvp&cdn_hash=38fc4020bce89428f80cb339d72ce094\",\n" +
            "\t\t\t\t\t\t\"240p\": \"https:\\/\\/19-10.b.cdn13.com\\/008\\/653\\/702\\/240p.h264.mp4?cdn_creation_time=1536847200&cdn_ttl=14400&cdn_bw=118k&cdn_bw_fs=312k&cdn_cv_data=77.180.197.184-dvp&cdn_hash=e1acd86ed3339c48bacf87937f1396e4\"\n" +
            "\t\t\t\t\t}\n" +
            "\t\t\t\t},\n" +
            "\t\t\t\t\"dimensions\": {\n" +
            "\t\t\t\t\t\"ext\": \"mp4\",\n" +
            "\t\t\t\t\t\"v\": 7,\n" +
            "\t\t\t\t\t\"cnt320p\": 50,\n" +
            "\t\t\t\t\t\"valid320p\": 40,\n" +
            "\t\t\t\t\t\"mediaInfo\": {\"keyframe\": 120},\n" +
            "\t\t\t\t\t\"watermark\": true,\n" +
            "\t\t\t\t\t\"trailer\": {\"version\": 8, \"160x120\": 102, \"240x180\": 168, \"320x180\": 198, \"320x240\": 234},\n" +
            "\t\t\t\t\t\"cdn\": 7,\n" +
            "\t\t\t\t\t\"video\": {\n" +
            "\t\t\t\t\t\t\"720p\": {\"h264\": 61583, \"vp9\": 43132, \"h265\": 31008},\n" +
            "\t\t\t\t\t\t\"480p\": {\"h264\": 32678},\n" +
            "\t\t\t\t\t\t\"240p\": {\"h264\": 16344}\n" +
            "\t\t\t\t\t},\n" +
            "\t\t\t\t\t\"ml-thumb\": 1,\n" +
            "\t\t\t\t\t\"ceph-cleaned\": 2\n" +
            "\t\t\t\t},\n" +
            "\t\t\t\t\"categories\": [{\n" +
            "\t\t\t\t\t\"name\": \"Naughty America\",\n" +
            "\t\t\t\t\t\"url\": \"\\/channels\\/naughty-america\",\n" +
            "\t\t\t\t\t\"sponsor-tag\": true,\n" +
            "\t\t\t\t\t\"pornstar\": false,\n" +
            "\t\t\t\t\t\"id\": \"40829\"\n" +
            "\t\t\t\t}, {\n" +
            "\t\t\t\t\t\"name\": \"Nina Elle\",\n" +
            "\t\t\t\t\t\"url\": \"\\/pornstars\\/nina-elle\",\n" +
            "\t\t\t\t\t\"sponsor-tag\": false,\n" +
            "\t\t\t\t\t\"pornstar\": true\n" +
            "\t\t\t\t}, {\n" +
            "\t\t\t\t\t\"name\": \"Abspritzen\",\n" +
            "\t\t\t\t\t\"url\": \"\\/categories\\/cumshot\",\n" +
            "\t\t\t\t\t\"sponsor-tag\": false,\n" +
            "\t\t\t\t\t\"pornstar\": false,\n" +
            "\t\t\t\t\t\"id\": \"18\"\n" +
            "\t\t\t\t}, {\n" +
            "\t\t\t\t\t\"name\": \"Blondinen\",\n" +
            "\t\t\t\t\t\"url\": \"\\/categories\\/blonde\",\n" +
            "\t\t\t\t\t\"sponsor-tag\": false,\n" +
            "\t\t\t\t\t\"pornstar\": false,\n" +
            "\t\t\t\t\t\"id\": \"11\"\n" +
            "\t\t\t\t}, {\n" +
            "\t\t\t\t\t\"name\": \"Cheating\",\n" +
            "\t\t\t\t\t\"url\": \"\\/categories\\/cheating\",\n" +
            "\t\t\t\t\t\"sponsor-tag\": false,\n" +
            "\t\t\t\t\t\"pornstar\": false,\n" +
            "\t\t\t\t\t\"id\": \"351\"\n" +
            "\t\t\t\t}, {\n" +
            "\t\t\t\t\t\"name\": \"Gro\\u00dfe Titten\",\n" +
            "\t\t\t\t\t\"url\": \"\\/categories\\/big-tits\",\n" +
            "\t\t\t\t\t\"sponsor-tag\": false,\n" +
            "\t\t\t\t\t\"pornstar\": false,\n" +
            "\t\t\t\t\t\"id\": \"671\"\n" +
            "\t\t\t\t}, {\n" +
            "\t\t\t\t\t\"name\": \"Gro\\u00dfer Schwanz\",\n" +
            "\t\t\t\t\t\"url\": \"\\/categories\\/big-cock\",\n" +
            "\t\t\t\t\t\"sponsor-tag\": false,\n" +
            "\t\t\t\t\t\"pornstar\": false,\n" +
            "\t\t\t\t\t\"id\": \"2807\"\n" +
            "\t\t\t\t}, {\n" +
            "\t\t\t\t\t\"name\": \"Videos HD\",\n" +
            "\t\t\t\t\t\"description\": null,\n" +
            "\t\t\t\t\t\"url\": \"\\/hd\",\n" +
            "\t\t\t\t\t\"sponsor-tag\": false,\n" +
            "\t\t\t\t\t\"pornstar\": false\n" +
            "\t\t\t\t}, {\n" +
            "\t\t\t\t\t\"name\": \"America Naughty\",\n" +
            "\t\t\t\t\t\"url\": \"\\/tags\\/america-naughty\",\n" +
            "\t\t\t\t\t\"sponsor-tag\": false,\n" +
            "\t\t\t\t\t\"pornstar\": false,\n" +
            "\t\t\t\t\t\"id\": \"131378\"\n" +
            "\t\t\t\t}, {\n" +
            "\t\t\t\t\t\"name\": \"Big Fat\",\n" +
            "\t\t\t\t\t\"url\": \"\\/tags\\/big-fat\",\n" +
            "\t\t\t\t\t\"sponsor-tag\": false,\n" +
            "\t\t\t\t\t\"pornstar\": false,\n" +
            "\t\t\t\t\t\"id\": \"12548\"\n" +
            "\t\t\t\t}, {\n" +
            "\t\t\t\t\t\"name\": \"Gro\\u00df\",\n" +
            "\t\t\t\t\t\"url\": \"\\/tags\\/big\",\n" +
            "\t\t\t\t\t\"sponsor-tag\": false,\n" +
            "\t\t\t\t\t\"pornstar\": false,\n" +
            "\t\t\t\t\t\"id\": \"77647\"\n" +
            "\t\t\t\t}, {\n" +
            "\t\t\t\t\t\"name\": \"Gro\\u00dfe Br\\u00fcste\",\n" +
            "\t\t\t\t\t\"url\": \"\\/tags\\/big-boobs\",\n" +
            "\t\t\t\t\t\"sponsor-tag\": false,\n" +
            "\t\t\t\t\t\"pornstar\": false,\n" +
            "\t\t\t\t\t\"id\": \"47\"\n" +
            "\t\t\t\t}, {\n" +
            "\t\t\t\t\t\"name\": \"In America\",\n" +
            "\t\t\t\t\t\"url\": \"\\/tags\\/in-america\",\n" +
            "\t\t\t\t\t\"sponsor-tag\": false,\n" +
            "\t\t\t\t\t\"pornstar\": false,\n" +
            "\t\t\t\t\t\"id\": \"105206\"\n" +
            "\t\t\t\t}, {\n" +
            "\t\t\t\t\t\"name\": \"In Face\",\n" +
            "\t\t\t\t\t\"url\": \"\\/tags\\/in-face\",\n" +
            "\t\t\t\t\t\"sponsor-tag\": false,\n" +
            "\t\t\t\t\t\"pornstar\": false,\n" +
            "\t\t\t\t\t\"id\": \"82396\"\n" +
            "\t\t\t\t}, {\n" +
            "\t\t\t\t\t\"name\": \"In Your Face\",\n" +
            "\t\t\t\t\t\"url\": \"\\/tags\\/in-your-face\",\n" +
            "\t\t\t\t\t\"sponsor-tag\": false,\n" +
            "\t\t\t\t\t\"pornstar\": false,\n" +
            "\t\t\t\t\t\"id\": \"10907\"\n" +
            "\t\t\t\t}, {\n" +
            "\t\t\t\t\t\"name\": \"Naughty\",\n" +
            "\t\t\t\t\t\"url\": \"\\/tags\\/naughty\",\n" +
            "\t\t\t\t\t\"sponsor-tag\": false,\n" +
            "\t\t\t\t\t\"pornstar\": false,\n" +
            "\t\t\t\t\t\"id\": \"8348\"\n" +
            "\t\t\t\t}],\n" +
            "\t\t\t\t\"sponsor\": {\n" +
            "\t\t\t\t\t\"id\": 1613,\n" +
            "\t\t\t\t\t\"site\": \"Naughty America\",\n" +
            "\t\t\t\t\t\"banner\": {\n" +
            "\t\t\t\t\t\t\"width\": 638,\n" +
            "\t\t\t\t\t\t\"height\": 60,\n" +
            "\t\t\t\t\t\t\"src\": \"https:\\/\\/thumb-v-cl.xhcdn.com\\/site\\/000\\/001\\/613\\/desktop.jpg.v1533717727\"\n" +
            "\t\t\t\t\t},\n" +
            "\t\t\t\t\t\"name\": \"\",\n" +
            "\t\t\t\t\t\"message\": \"Find your fantasy with todays hottest porn stars\",\n" +
            "\t\t\t\t\t\"link\": \"https:\\/\\/natour.naughtyamerica.com\\/track\\/xhamster.12.8.8.0.0.0.0.0\",\n" +
            "\t\t\t\t\t\"landing\": 0,\n" +
            "\t\t\t\t\t\"description\": \"\"\n" +
            "\t\t\t\t},\n" +
            "\t\t\t\t\"reported\": false,\n" +
            "\t\t\t\t\"editable\": false,\n" +
            "\t\t\t\t\"subscriptionModel\": {\n" +
            "\t\t\t\t\t\"modelName\": \"channelModel\",\n" +
            "\t\t\t\t\t\"id\": 40829,\n" +
            "\t\t\t\t\t\"subscribed\": false,\n" +
            "\t\t\t\t\t\"subscribers\": 8382\n" +
            "\t\t\t\t},\n" +
            "\t\t\t\t\"author\": {\n" +
            "\t\t\t\t\t\"modelName\": \"shortUserModel\",\n" +
            "\t\t\t\t\t\"id\": 2942338,\n" +
            "\t\t\t\t\t\"pageURL\": \"https:\\/\\/de.xhamster.com\\/users\\/naughtyamerica\",\n" +
            "\t\t\t\t\t\"retired\": false,\n" +
            "\t\t\t\t\t\"verified\": false,\n" +
            "\t\t\t\t\t\"name\": \"NaughtyAmerica\"\n" +
            "\t\t\t\t}\n" +
            "\t\t\t}"

    @Test
    fun testParseJsonString() {
        val videoModel=VideoModelParser().fromString(jsonString)
        Assert.assertNotNull(videoModel)
    }
}