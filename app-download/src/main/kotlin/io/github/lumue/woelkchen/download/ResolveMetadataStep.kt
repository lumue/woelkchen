package io.github.lumue.woelkchen.download

interface ResolveMetadataStep {

      suspend fun retrieveMetadata(l: MediaLocation) : LocationMetadata

}





class ResolveException(s: String,e: Throwable? =null) : Throwable(s,e)