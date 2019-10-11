package io.github.lumue.woelkchen.download

import io.github.lumue.woelkchen.shared.metadata.MoviepageMetadata

interface ResolveMetadataStep {

      suspend fun retrieveMetadata(l: MediaLocation) : MoviepageMetadata

}





class ResolveException(s: String,e: Throwable? =null) : Throwable(s,e)