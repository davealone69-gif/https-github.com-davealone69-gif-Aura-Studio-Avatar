package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.swarm.SwarmNode
import com.example.ui.theme.BorderGlass
import com.example.ui.theme.CardGlass
import com.example.ui.theme.CrimsonFire
import com.example.ui.theme.CyanNeon
import com.example.ui.theme.DeepMidnight
import com.example.ui.theme.EmeraldNeon
import com.example.ui.theme.GoldCelestial
import com.example.ui.theme.MagentaGlow
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.MainViewModel

@Composable
fun SwarmConsoleScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val lastRouting by viewModel.lastRouting.collectAsState()

    var testJsonInput by remember {
        mutableStateOf(
            """{
  "target_node": "AGENT_UNKNOWN",
  "intent_detected": "Broken payload test",
  "execution_payload": {
    "primary_instruction": "Test missing bracket
}"""
        )
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DeepMidnight)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Column {
            Text(
                text = "SWARM ORCHESTRATOR CONSOLE",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = EmeraldNeon,
                letterSpacing = 2.sp
            )
            Text(
                text = "SWARM_MASTER & SWARM_HEALER",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextPrimary
            )
        }

        // Swarm Sub-Agent Node Status Grid
        Card(
            colors = CardDefaults.cardColors(containerColor = CardGlass),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderGlass),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Hub,
                        contentDescription = "Swarm Grid",
                        tint = EmeraldNeon,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "ACTIVE SWARM NODES (4/4 ONLINE)",
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        fontSize = 13.sp
                    )
                }

                SwarmNode.entries.forEach { node ->
                    val isActive = lastRouting?.targetNode == node
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isActive) SurfaceDark else Color.Transparent)
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(if (isActive) CyanNeon else EmeraldNeon)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = node.nodeName,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isActive) CyanNeon else TextPrimary
                            )
                            Text(
                                text = node.description,
                                fontSize = 10.sp,
                                color = TextSecondary
                            )
                        }

                        if (isActive) {
                            Text(
                                text = "ACTIVE ROUTE",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = CyanNeon,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(CyanNeon.copy(alpha = 0.15f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }

        // Live JSON Schema Output Console
        Card(
            colors = CardDefaults.cardColors(containerColor = SurfaceDark),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, CyanNeon.copy(alpha = 0.6f)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Code,
                            contentDescription = "JSON Console",
                            tint = CyanNeon,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "SWARM_MASTER JSON SCHEMATIC",
                            fontWeight = FontWeight.Bold,
                            color = CyanNeon,
                            fontSize = 12.sp
                        )
                    }

                    if (lastRouting?.isSelfHealed == true) {
                        Text(
                            text = "SELF_HEALED",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = CrimsonFire
                        )
                    } else {
                        Text(
                            text = "SCHEMA VALID",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldNeon
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Black.copy(alpha = 0.85f))
                        .padding(12.dp)
                ) {
                    Text(
                        text = lastRouting?.rawJsonOutput ?: "No routing payload captured yet. Send a message in Swarm Chat!",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        color = EmeraldNeon
                    )
                }
            }
        }

        // SWARM_HEALER Inspector & Test Injector
        Card(
            colors = CardDefaults.cardColors(containerColor = CardGlass),
            border = androidx.compose.foundation.BorderStroke(1.dp, CrimsonFire.copy(alpha = 0.6f)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = "SWARM_HEALER",
                        tint = CrimsonFire,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "SWARM_HEALER INSPECTOR & REPAIR",
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        fontSize = 13.sp
                    )
                }

                Text(
                    text = "Test autonomous schema validation and error-correction on malformed or broken JSON payloads:",
                    fontSize = 11.sp,
                    color = TextSecondary
                )

                OutlinedTextField(
                    value = testJsonInput,
                    onValueChange = { testJsonInput = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("healer_test_json_field"),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CrimsonFire,
                        unfocusedBorderColor = BorderGlass,
                        focusedContainerColor = SurfaceDark,
                        unfocusedContainerColor = SurfaceDark,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    maxLines = 5,
                    textStyle = androidx.compose.ui.text.TextStyle(fontFamily = FontFamily.Monospace, fontSize = 11.sp)
                )

                Button(
                    onClick = { viewModel.testSelfHealer(testJsonInput) },
                    colors = ButtonDefaults.buttonColors(containerColor = CrimsonFire),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("trigger_healer_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoFixHigh,
                        contentDescription = null,
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("RUN SWARM_HEALER REPAIR", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}
