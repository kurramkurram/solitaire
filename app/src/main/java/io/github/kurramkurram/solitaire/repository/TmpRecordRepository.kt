package io.github.kurramkurram.solitaire.repository

import android.content.Context
import io.github.kurramkurram.solitaire.data.Record
import io.github.kurramkurram.solitaire.database.TmpRecordDatabase

/**
 * 記録の復元リポジトリ.
 */
abstract class TmpRecordRepository {

    /**
     * 復元用のDatabaseから全件取得する.
     *
     * @return 記録情報
     */
    abstract suspend fun selectAll(): MutableList<Record>
}

class TmpRecordRepositoryImpl(
    context: Context,
    private val db: TmpRecordDatabase = TmpRecordDatabase.getDatabases(context)
) : TmpRecordRepository() {

    override suspend fun selectAll(): MutableList<Record> {
        val dao = db.recordDao()
        return dao.selectAll()
    }
}
