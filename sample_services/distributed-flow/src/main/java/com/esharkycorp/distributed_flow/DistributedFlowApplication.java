package com.esharkycorp.distributed_flow;

import com.hilabs.cdi.common.Constants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.esharkycorp.distributed_flow", Constants.COMMON_PACKAGE_BASE_PATH})
public class DistributedFlowApplication {
	public static void main(String[] args) {
		SpringApplication.run(DistributedFlowApplication.class, args);
	}
}
