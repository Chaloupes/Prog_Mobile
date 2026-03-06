import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.insa.mygameslist.GameDetails
import com.insa.mygameslist.ListGameCell
import com.insa.mygameslist.data.Game
import com.insa.mygameslist.data.IGDB



@Composable
fun appNavigation(data: IGDB, stack: SnapshotStateList<Any>) {

    val backStack = remember { stack }

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = { key ->
            when (key) {
                is String -> NavEntry(key) {
                    ListGameCell(data)
                }

                is Game ->  NavEntry(
                    key,
                    metadata = mapOf("extraDataKey" to "extraDataValue")
                ) { GameDetails(key,data)}

                else -> error("Unknown route")
            }
        }
    )
}

