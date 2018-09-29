package io.github.lumue.mc.dlservice

interface LocationMetadataResolver {

      suspend fun resolveMetadata(l: MediaLocation) : LocationMetadata

}





