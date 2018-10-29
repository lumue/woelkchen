package io.github.lumue.woelkchen.download

interface ResolveMetadataStep {

      suspend fun resolveMetadata(l: MediaLocation) : LocationMetadata

}





