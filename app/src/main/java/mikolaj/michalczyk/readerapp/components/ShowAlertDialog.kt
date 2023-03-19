package mikolaj.michalczyk.readerapp.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ShowAlertDialog(
                      message: String,
                      openDialog: MutableState<Boolean>,
                      onYesPressed: () -> Unit) {
   if(openDialog.value){
    AlertDialog(onDismissRequest = { openDialog.value = false },
    title = { Text(text = "Delete Book")},
    text = { Text(text = message)},
    buttons = {
        Row(modifier = Modifier.padding(all = 8.dp),
        horizontalArrangement = Arrangement.Center) {
            TextButton(onClick = { onYesPressed.invoke()}) {
                Text(text = "Yes")
                
            }
            Spacer(modifier = Modifier.width(50.dp))
            TextButton(onClick = { openDialog.value = false}) {
                Text(text = "No")
            }
        }
    })
   }
}
