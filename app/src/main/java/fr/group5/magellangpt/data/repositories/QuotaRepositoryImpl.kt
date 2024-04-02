package fr.group5.magellangpt.data.repositories

import android.util.Log
import com.microsoft.signalr.HubConnection
import fr.group5.magellangpt.data.remote.dto.down.QuotaDtoDown
import fr.group5.magellangpt.domain.models.Quota
import fr.group5.magellangpt.domain.repositories.QuotaRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.get

class QuotaRepositoryImpl(
    private val hubConnection: HubConnection = get(HubConnection::class.java)
) : QuotaRepository {

    override suspend fun getCurrentQuota(): Flow<Quota> = callbackFlow {
//        hubConnection.start()
//
//        hubConnection.on("general",
//            { value ->
//                Log.d("QuotaRepositoryImpl", value.maxQuota.toString())
//                trySend(Quota(value.currentQuota.toInt(), value.maxQuota.toInt()))
//            },
//            QuotaDtoDown::class.java,
//        )
//
        awaitClose {
//            hubConnection.stop()
        }
    }
}