package com.example

import com.example.swarm.SwarmHealer
import com.example.swarm.SwarmMaster
import com.example.swarm.SwarmNode
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class SwarmEngineTest {

    @Test
    fun testSwarmMasterRoutingChat() {
        val decision = SwarmMaster.routeInput("Hello Aura, how are you today?")
        assertEquals(SwarmNode.AGENT_CHAT, decision.targetNode)
        assertTrue(decision.confidenceScore > 0.8)
    }

    @Test
    fun testSwarmMasterRoutingMedia() {
        val decision = SwarmMaster.routeInput("Generate an 8K image prompt of a cyberpunk city")
        assertEquals(SwarmNode.AGENT_MEDIA, decision.targetNode)
    }

    @Test
    fun testSwarmMasterRoutingAvatar() {
        val decision = SwarmMaster.routeInput("Change my hairstyle and outfit color")
        assertEquals(SwarmNode.AGENT_AVATAR, decision.targetNode)
    }

    @Test
    fun testSwarmHealerValidation() {
        val malformedJson = "{ \"target_node\": \"AGENT_BROKEN\", \"invalid_json\": "
        val healed = SwarmHealer.validateAndHeal(malformedJson)
        assertTrue(healed.isSelfHealed)
        assertEquals(SwarmNode.AGENT_CHAT, healed.targetNode)
        assertNotNull(healed.auditLog)
    }
}
