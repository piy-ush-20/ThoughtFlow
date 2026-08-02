package com.piyush.thoughtflow.`data`.local

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class DocumentDao_Impl(
  __db: RoomDatabase,
) : DocumentDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfDocumentEntity: EntityInsertAdapter<DocumentEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfDocumentEntity = object : EntityInsertAdapter<DocumentEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `documents` (`id`,`title`,`bodyMarkdown`,`createdAtEpochMs`,`updatedAtEpochMs`,`status`,`wordCount`,`lastExportAtEpochMs`,`formatterUsed`) VALUES (?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: DocumentEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.title)
        statement.bindText(3, entity.bodyMarkdown)
        statement.bindLong(4, entity.createdAtEpochMs)
        statement.bindLong(5, entity.updatedAtEpochMs)
        statement.bindText(6, entity.status)
        statement.bindLong(7, entity.wordCount.toLong())
        val _tmpLastExportAtEpochMs: Long? = entity.lastExportAtEpochMs
        if (_tmpLastExportAtEpochMs == null) {
          statement.bindNull(8)
        } else {
          statement.bindLong(8, _tmpLastExportAtEpochMs)
        }
        val _tmpFormatterUsed: String? = entity.formatterUsed
        if (_tmpFormatterUsed == null) {
          statement.bindNull(9)
        } else {
          statement.bindText(9, _tmpFormatterUsed)
        }
      }
    }
  }

  public override suspend fun upsert(entity: DocumentEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfDocumentEntity.insert(_connection, entity)
  }

  public override fun observeAll(): Flow<List<DocumentEntity>> {
    val _sql: String = "SELECT * FROM documents ORDER BY updatedAtEpochMs DESC"
    return createFlow(__db, false, arrayOf("documents")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfBodyMarkdown: Int = getColumnIndexOrThrow(_stmt, "bodyMarkdown")
        val _columnIndexOfCreatedAtEpochMs: Int = getColumnIndexOrThrow(_stmt, "createdAtEpochMs")
        val _columnIndexOfUpdatedAtEpochMs: Int = getColumnIndexOrThrow(_stmt, "updatedAtEpochMs")
        val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
        val _columnIndexOfWordCount: Int = getColumnIndexOrThrow(_stmt, "wordCount")
        val _columnIndexOfLastExportAtEpochMs: Int = getColumnIndexOrThrow(_stmt, "lastExportAtEpochMs")
        val _columnIndexOfFormatterUsed: Int = getColumnIndexOrThrow(_stmt, "formatterUsed")
        val _result: MutableList<DocumentEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: DocumentEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpBodyMarkdown: String
          _tmpBodyMarkdown = _stmt.getText(_columnIndexOfBodyMarkdown)
          val _tmpCreatedAtEpochMs: Long
          _tmpCreatedAtEpochMs = _stmt.getLong(_columnIndexOfCreatedAtEpochMs)
          val _tmpUpdatedAtEpochMs: Long
          _tmpUpdatedAtEpochMs = _stmt.getLong(_columnIndexOfUpdatedAtEpochMs)
          val _tmpStatus: String
          _tmpStatus = _stmt.getText(_columnIndexOfStatus)
          val _tmpWordCount: Int
          _tmpWordCount = _stmt.getLong(_columnIndexOfWordCount).toInt()
          val _tmpLastExportAtEpochMs: Long?
          if (_stmt.isNull(_columnIndexOfLastExportAtEpochMs)) {
            _tmpLastExportAtEpochMs = null
          } else {
            _tmpLastExportAtEpochMs = _stmt.getLong(_columnIndexOfLastExportAtEpochMs)
          }
          val _tmpFormatterUsed: String?
          if (_stmt.isNull(_columnIndexOfFormatterUsed)) {
            _tmpFormatterUsed = null
          } else {
            _tmpFormatterUsed = _stmt.getText(_columnIndexOfFormatterUsed)
          }
          _item = DocumentEntity(_tmpId,_tmpTitle,_tmpBodyMarkdown,_tmpCreatedAtEpochMs,_tmpUpdatedAtEpochMs,_tmpStatus,_tmpWordCount,_tmpLastExportAtEpochMs,_tmpFormatterUsed)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun observeById(id: String): Flow<DocumentEntity?> {
    val _sql: String = "SELECT * FROM documents WHERE id = ? LIMIT 1"
    return createFlow(__db, false, arrayOf("documents")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfBodyMarkdown: Int = getColumnIndexOrThrow(_stmt, "bodyMarkdown")
        val _columnIndexOfCreatedAtEpochMs: Int = getColumnIndexOrThrow(_stmt, "createdAtEpochMs")
        val _columnIndexOfUpdatedAtEpochMs: Int = getColumnIndexOrThrow(_stmt, "updatedAtEpochMs")
        val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
        val _columnIndexOfWordCount: Int = getColumnIndexOrThrow(_stmt, "wordCount")
        val _columnIndexOfLastExportAtEpochMs: Int = getColumnIndexOrThrow(_stmt, "lastExportAtEpochMs")
        val _columnIndexOfFormatterUsed: Int = getColumnIndexOrThrow(_stmt, "formatterUsed")
        val _result: DocumentEntity?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpBodyMarkdown: String
          _tmpBodyMarkdown = _stmt.getText(_columnIndexOfBodyMarkdown)
          val _tmpCreatedAtEpochMs: Long
          _tmpCreatedAtEpochMs = _stmt.getLong(_columnIndexOfCreatedAtEpochMs)
          val _tmpUpdatedAtEpochMs: Long
          _tmpUpdatedAtEpochMs = _stmt.getLong(_columnIndexOfUpdatedAtEpochMs)
          val _tmpStatus: String
          _tmpStatus = _stmt.getText(_columnIndexOfStatus)
          val _tmpWordCount: Int
          _tmpWordCount = _stmt.getLong(_columnIndexOfWordCount).toInt()
          val _tmpLastExportAtEpochMs: Long?
          if (_stmt.isNull(_columnIndexOfLastExportAtEpochMs)) {
            _tmpLastExportAtEpochMs = null
          } else {
            _tmpLastExportAtEpochMs = _stmt.getLong(_columnIndexOfLastExportAtEpochMs)
          }
          val _tmpFormatterUsed: String?
          if (_stmt.isNull(_columnIndexOfFormatterUsed)) {
            _tmpFormatterUsed = null
          } else {
            _tmpFormatterUsed = _stmt.getText(_columnIndexOfFormatterUsed)
          }
          _result = DocumentEntity(_tmpId,_tmpTitle,_tmpBodyMarkdown,_tmpCreatedAtEpochMs,_tmpUpdatedAtEpochMs,_tmpStatus,_tmpWordCount,_tmpLastExportAtEpochMs,_tmpFormatterUsed)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getById(id: String): DocumentEntity? {
    val _sql: String = "SELECT * FROM documents WHERE id = ? LIMIT 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfBodyMarkdown: Int = getColumnIndexOrThrow(_stmt, "bodyMarkdown")
        val _columnIndexOfCreatedAtEpochMs: Int = getColumnIndexOrThrow(_stmt, "createdAtEpochMs")
        val _columnIndexOfUpdatedAtEpochMs: Int = getColumnIndexOrThrow(_stmt, "updatedAtEpochMs")
        val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
        val _columnIndexOfWordCount: Int = getColumnIndexOrThrow(_stmt, "wordCount")
        val _columnIndexOfLastExportAtEpochMs: Int = getColumnIndexOrThrow(_stmt, "lastExportAtEpochMs")
        val _columnIndexOfFormatterUsed: Int = getColumnIndexOrThrow(_stmt, "formatterUsed")
        val _result: DocumentEntity?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpBodyMarkdown: String
          _tmpBodyMarkdown = _stmt.getText(_columnIndexOfBodyMarkdown)
          val _tmpCreatedAtEpochMs: Long
          _tmpCreatedAtEpochMs = _stmt.getLong(_columnIndexOfCreatedAtEpochMs)
          val _tmpUpdatedAtEpochMs: Long
          _tmpUpdatedAtEpochMs = _stmt.getLong(_columnIndexOfUpdatedAtEpochMs)
          val _tmpStatus: String
          _tmpStatus = _stmt.getText(_columnIndexOfStatus)
          val _tmpWordCount: Int
          _tmpWordCount = _stmt.getLong(_columnIndexOfWordCount).toInt()
          val _tmpLastExportAtEpochMs: Long?
          if (_stmt.isNull(_columnIndexOfLastExportAtEpochMs)) {
            _tmpLastExportAtEpochMs = null
          } else {
            _tmpLastExportAtEpochMs = _stmt.getLong(_columnIndexOfLastExportAtEpochMs)
          }
          val _tmpFormatterUsed: String?
          if (_stmt.isNull(_columnIndexOfFormatterUsed)) {
            _tmpFormatterUsed = null
          } else {
            _tmpFormatterUsed = _stmt.getText(_columnIndexOfFormatterUsed)
          }
          _result = DocumentEntity(_tmpId,_tmpTitle,_tmpBodyMarkdown,_tmpCreatedAtEpochMs,_tmpUpdatedAtEpochMs,_tmpStatus,_tmpWordCount,_tmpLastExportAtEpochMs,_tmpFormatterUsed)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun delete(id: String) {
    val _sql: String = "DELETE FROM documents WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun markExported(id: String, exportedAt: Long) {
    val _sql: String = "UPDATE documents SET lastExportAtEpochMs = ? WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, exportedAt)
        _argIndex = 2
        _stmt.bindText(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
