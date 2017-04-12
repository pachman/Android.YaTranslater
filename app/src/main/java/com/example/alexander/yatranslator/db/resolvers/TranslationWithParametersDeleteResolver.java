package com.example.alexander.yatranslator.db.resolvers;

import android.support.annotation.NonNull;
import com.example.alexander.yatranslator.db.entities.TranslationItem;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.delete.DeleteResolver;
import com.pushtorefresh.storio.sqlite.operations.delete.DeleteResult;

/**
 * Created by Alexander on 09.04.2017.
 */
public class TranslationWithParametersDeleteResolver extends DeleteResolver<TranslationItem> {
    @NonNull
    @Override
    public DeleteResult performDelete(@NonNull StorIOSQLite storIOSQLite, @NonNull TranslationItem object) {
/*        // 1 for user and other for his/her tweets
        final List<Object> objectsToDelete = new ArrayList<Object>(1 + userWithTweets.tweets().size());

        objectsToDelete.add(userWithTweets.user());
        objectsToDelete.addAll(userWithTweets.tweets());

        storIOSQLite
                .delete()
                .objects(objectsToDelete)
                .prepare()
                .executeAsBlocking();

        // BTW, you can save it as static final
        final Set<String> affectedTables = new HashSet<String>(2);

        affectedTables.add(UsersTable.TABLE);
        affectedTables.add(TweetsTable.TABLE);

        return DeleteResult.newInstance(objectsToDelete.size(), affectedTables);*/

        return null;
    }
}
