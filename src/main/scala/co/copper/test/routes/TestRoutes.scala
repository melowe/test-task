package co.copper.test.routes

import scala.compat.java8.FutureConverters.CompletionStageOps

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import com.sbuslab.http.RestRoutes
import com.sbuslab.sbus.Context

import co.copper.test.services.{TestJavaService, TestService}

@Component
@Autowired
class TestRoutes(
  deribitService: TestService,
  deribitJavaService: TestJavaService,
) extends RestRoutes {

  def anonymousRoutes(implicit context: Context) =
    pathEnd {
      post {
        complete {
          deribitJavaService.queryAndSave().toScala;
        }
      } ~
      get {
        complete {
          deribitJavaService.getOk.toScala
        }
      }
    }

}
