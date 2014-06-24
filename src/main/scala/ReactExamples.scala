package golly

import scala.scalajs.js
import org.scalajs.dom.{Node, document, console, window}

object ReactExamples {

//  object Sample1 {
//
//    case class HelloProps(name: String, age: Int)
//
//    def apply(): Unit = {
//      import react.scalatags.ReactDom._
//      import all._
//
//      val renderFn = RenderFn.wrapped[HelloProps](props =>
//        div(backgroundColor := "#fdd", color := "#c00")(
//          h1("THIS IS AWESOME"),
//          p(textDecoration := "underline")("Hello there, ", "Hello, ", props.name, " of age ", props.age)
//        ).render
//      )
//
//      val HelloMessage = React.createClass(ComponentSpec(renderFn))
//      val pc = HelloMessage(HelloProps("Johnhy", 100))
//
//      val tgt = document.getElementById("target")
//      React.renderComponent(pc, tgt)
//    }
//  }
  
  // ===================================================================================================================

  object Sample2 {
    import react._
    import react.scalatags.ReactDom._
    import all._

    case class MyProps(title: String, startTime: Long)

    case class MyState(secondsElapsed: Long) {
      def inc = MyState(secondsElapsed + 1)
    }

    class MyBackend {
      var interval: js.UndefOr[Int] = js.undefined
      def start(tick: js.Function): Unit = interval = window.setInterval(tick, 1000)
      def stop(): Unit = interval foreach window.clearInterval
    }

    val specBuilder = new ComponentSpecBuilder[WrapObj[MyProps], WrapObj[MyState], MyBackend]
    def spec = specBuilder
      .init(new MyBackend,
        ctx =>
          div(backgroundColor := "#fdd", color := "#c00")(
            h1("THIS IS AWESOME"),
            p(textDecoration := "underline")(ctx.props.title, ". Seconds elapsed: ", ctx.state.secondsElapsed)
          ).render
      )
      .getInitialState(ctx => MyState(ctx.props.startTime).wrap)
      .componentDidMount(ctx => {
        val tick: js.Function = (_: js.Any) => ctx.modState(_.inc.wrap)
        console log "Installing timer..."
        ctx.backend.start(tick)
      })
      .componentWillUnmount(_.backend.stop)
      .build

    def apply(): Unit = {
      val c = React.createClass(spec)
      React.renderComponent(c(MyProps("Great", 0).wrap), document getElementById "target")
      React.renderComponent(c(MyProps("Again", 1000).wrap), document getElementById "target2")
    }
  }
}