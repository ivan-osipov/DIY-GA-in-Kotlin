package runner

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import model.createData

class Main : Application() {

    companion object {
        val TITLE = "EduScheduler"

        @JvmStatic
        fun main(args: Array<String>) {
            Main().run(args)
        }
    }

    fun run(args: Array<String>) {
        launch(*args)
    }

    private val data = createData()

    override fun start(primaryStage: Stage) {
        with(primaryStage) {
            primaryStage.title = TITLE
            val root = VBox()
            initLayout(root)
            scene = Scene(root, 300.0, 150.0)
            show()
        }
    }

    private fun initLayout(root: Pane) {
        val runButton = Button("Schedule")
        root.children.add(runButton)
        runButton.setOnAction {

        }
    }

}
