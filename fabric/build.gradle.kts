import net.fabricmc.loom.api.mappings.layered.MappingContext
import net.fabricmc.loom.api.mappings.layered.MappingLayer
import net.fabricmc.loom.api.mappings.layered.MappingsNamespace
import net.fabricmc.loom.api.mappings.layered.spec.MappingsSpec
import net.fabricmc.loom.configuration.providers.mappings.intermediary.IntermediaryMappingLayer
import net.fabricmc.mappingio.MappingVisitor
import net.fabricmc.mappingio.tree.MappingTreeView
import net.fabricmc.mappingio.tree.MemoryMappingTree

plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"

}

architectury {
    platformSetupLoomIde()
    fabric()
}

loom {
    accessWidenerPath.set(project(":common").loom.accessWidenerPath)
}

val common: Configuration by configurations.creating
val shadowCommon: Configuration by configurations.creating

configurations {
    compileClasspath.get().extendsFrom(common)
    runtimeClasspath.get().extendsFrom(common)
    named("developmentFabric").get().extendsFrom(common)
}

dependencies {
    "mappings"(loom.layered {
        officialMojangMappings()
        addLayer(object : MappingsSpec<MappingLayer> {
            val getClasses = MappingTreeView::class.java.getDeclaredMethod("getClasses")
                    .apply { isAccessible = true }
            val getMethods = MappingTreeView.ClassMappingView::class.java.getDeclaredMethod("getMethods")
                    .apply { isAccessible = true }
            val getName = MappingTreeView.ElementMappingView::class.java.getDeclaredMethod("getName", String::class.java)
                    .apply { isAccessible = true }
            val entryClass = Class.forName("net.fabricmc.mappingio.tree.MemoryMappingTree\$Entry")
            val srcNameField = entryClass.getDeclaredField("srcName")
                    .apply { isAccessible = true }

            val METHOD_NAME_MAP = mapOf("getTextureLocation" to "_getTextureLocation")
            override fun createLayer(context: MappingContext?): MappingLayer {
                return object : MappingLayer {
                    override fun visit(mappingVisitor: MappingVisitor?) {
                        val memoryMappingTree = mappingVisitor as MemoryMappingTree
                        (getClasses(memoryMappingTree) as Collection<*>).forEach { classEntry ->
                            (getMethods(classEntry) as Collection<*>).forEach { methodEntry ->
                                METHOD_NAME_MAP[getName(methodEntry, MappingsNamespace.NAMED.toString())]?.let {
                                    srcNameField[methodEntry] = it
                                }
                            }
                        }
                    }

                    override fun getSourceNamespace() = MappingsNamespace.NAMED

                    override fun dependsOn() = listOf(IntermediaryMappingLayer::class.java)
                }
            }

            override fun hashCode() = METHOD_NAME_MAP.hashCode()
        })
    })
    modImplementation ("net.fabricmc:fabric-loader:0.16.5")
    modApi ("net.fabricmc.fabric-api:fabric-api:0.76.0+1.18.2")
    // Remove the next line if you don't want to depend on the API
    modApi ("dev.architectury:architectury-fabric:4.12.94")
    modApi("teamreborn:energy:2.3.0")
    modImplementation ("curse.maven:more-hitboxes-1115989:6203363")
    modImplementation ("curse.maven:geckolib-388172:4181373")
    modImplementation ("curse.maven:terrablender-fabric-565956:3957975")
    modImplementation ("curse.maven:fossils-223908:6204260")
    modImplementation("dev.onyxstudios.cardinal-components-api:cardinal-components-base:4.2.0")
    modImplementation("dev.onyxstudios.cardinal-components-api:cardinal-components-entity:4.2.0")
    modImplementation("maven.modrinth:midnightlib:0.4.4")
    modImplementation("maven.modrinth:Wd844r7Q:1.18.2-02")
    modImplementation("maven.modrinth:farmers-delight-fabric:1.2.5")
    implementation("com.electronwill.night-config:core:3.6.6")
    implementation("com.electronwill.night-config:toml:3.6.6")

    //dev only
    modImplementation("curse.maven:modmenu-308702:4145213")

    common(project(path = ":common", configuration = "namedElements")) { isTransitive = false }
    shadowCommon(project(path = ":common", configuration = "transformProductionFabric")) { isTransitive = false }
}
tasks {
    processResources {
        inputs.property("version", project.version)
        filesMatching("fabric.mod.json") {
            expand(mutableMapOf("version" to project.version))
        }
    }
    shadowJar {
        configurations = listOf(shadowCommon)
        archiveClassifier.set("dev-shadow")

    }



    remapJar {
        injectAccessWidener.set(true)
        inputFile.set(shadowJar.get().archiveFile)
        dependsOn(shadowJar)
    }
}

val javaComponent = components["java"] as AdhocComponentWithVariants
javaComponent.withVariantsFromConfiguration(configurations["shadowRuntimeElements"]) {
    skip()
}
