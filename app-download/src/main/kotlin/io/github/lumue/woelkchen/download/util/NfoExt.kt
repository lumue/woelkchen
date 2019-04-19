package io.github.lumue.woelkchen.download.util

import io.github.lumue.woelkchen.download.LocationMetadata
import io.github.lumue.nfotools.Movie

 fun LocationMetadata.configureMovieBuilderWithLocationMetadata(movieBuilder: Movie.MovieBuilder) {
    movieBuilder
        .withAired(contentMetadata.uploaded)
        .withDateAdded(contentMetadata.downloaded)
        .withTitle(contentMetadata.title)
        .withRuntime(contentMetadata.duration.toMinutes().toString())
        .withTag(this.contentMetadata.hoster)
        .withTag(this.contentMetadata.uploaded?.year.toString())
        .withTag(this.contentMetadata.uploaded?.month.toString().toLowerCase())
        .withVotes(this.contentMetadata.votes.toString())
        .withTagline(this.contentMetadata.description)

     this.contentMetadata.tags
            .map { tag ->  tag.name}
            .map{tagstring->tagstring.replace("/tags/","") }
            .map { tagstring->tagstring.replace("/categories/","") }
            .map { tagstring->tagstring.replace("/channels/","") }
            .map { tagstring->tagstring.replace("/","") }
            .forEach{movieBuilder.withTag(it)}
     this.contentMetadata.actors
             .map { actor -> Movie.Actor(actor.name,"") }
             .forEach { actor->movieBuilder.addActor(actor) }
}

