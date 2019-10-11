import io.github.lumue.woelkchen.media.data.Movie
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface MovieRepository : PagingAndSortingRepository<Movie, String> {

    fun findByTitle(@Param("title") title: String): Movie?

    fun findByTitleContaining(title: String): Collection<Movie>

}