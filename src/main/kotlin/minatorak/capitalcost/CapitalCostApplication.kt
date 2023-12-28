package minatorak.capitalcost

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class CapitalCostApplication

fun main(args: Array<String>) {
	runApplication<CapitalCostApplication>(*args)
}
