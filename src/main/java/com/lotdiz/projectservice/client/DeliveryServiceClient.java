package com.lotdiz.projectservice.client;

import com.lotdiz.projectservice.dto.response.DeliveryStatusResponseDto;
import com.lotdiz.projectservice.utils.SuccessResponse;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

//@FeignClient(name = "deliveryServiceClient", url = "${endpoint.delivery-service}")
@FeignClient(name = "deliveryServiceClient", url = "https://e53d10c6-1cf7-4b4b-8ffb-47f7b1ed3862.mock.pstmn.io")
public interface DeliveryServiceClient {
  @PostMapping("/delivery/status")
  SuccessResponse<DeliveryStatusResponseDto> getDeliveryStatus(@RequestBody List<Long> fundingId);
}
