package com.zaze.demo.feature.storage

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zaze.core.designsystem.compose.components.BackIconButton
import com.zaze.demo.feature.storage.navigation.dataStoreRoute
import com.zaze.demo.feature.storage.navigation.sharedPreferencesRoute
import com.zaze.demo.feature.storage.provider.ProviderActivity

@Composable
fun StorageRoute(
    onBackPress: () -> Unit,
    onNavigateToDestination: (String) -> Unit
) {
    StorageScreen(onBackPress, onNavigateToDestination)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StorageScreen(
    onBackPress: () -> Unit,
    onNavigateToDestination: (String) -> Unit
) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(stringResource(R.string.storage))
                },
                navigationIcon = {
                    BackIconButton(onBackPress = onBackPress)
                }
            )
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            ElevatedButton(modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp, 8.dp),
                onClick = {
                    onNavigateToDestination(sharedPreferencesRoute)
                }
            ) {
                Text("SharedPreferences")
            }
            ElevatedButton(modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp, 8.dp),
                onClick = {
                    onNavigateToDestination(dataStoreRoute)
                }
            ) {
                Text("DataStore")
            }
            ElevatedButton(modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp, 8.dp),
                onClick = {
                    context.startActivity(Intent(context, ProviderActivity::class.java))
                }
            ) {
                Text("ContentProvider")
            }
        }
    }
}