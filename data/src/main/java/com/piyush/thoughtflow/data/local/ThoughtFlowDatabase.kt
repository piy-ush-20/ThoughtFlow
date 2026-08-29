package com.piyush.thoughtflow.data.local

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "documents")
data class DocumentEntity(
    @PrimaryKey val id: String,
    val title: String,
    val bodyMarkdown: String,
    val createdAtEpochMs: Long,
    val updatedAtEpochMs: Long,
    val status: String,
    val wordCount: Int,
    val lastExportAtEpochMs: Long?,
    val formatterUsed: String?,
)

@Dao
interface DocumentDao {
    @Query("SELECT * FROM documents ORDER BY updatedAtEpochMs DESC")
    fun observeAll(): Flow<List<DocumentEntity>>

    @Query("SELECT * FROM documents WHERE id = :id LIMIT 1")
    fun observeById(id: String): Flow<DocumentEntity?>

    @Query("SELECT * FROM documents WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): DocumentEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: DocumentEntity)

    @Query("DELETE FROM documents WHERE id = :id")
    suspend fun delete(id: String)

    @Query("UPDATE documents SET lastExportAtEpochMs = :exportedAt WHERE id = :id")
    suspend fun markExported(id: String, exportedAt: Long)
}

@Database(
    entities = [DocumentEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class ThoughtFlowDatabase : RoomDatabase() {
    abstract fun documentDao(): DocumentDao
}
