package com.lotdiz.projectservice.client;

import com.lotdiz.projectservice.dto.response.DeliveryStatusResponseDto;
import com.lotdiz.projectservice.utils.SuccessResponse;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "deliveryServiceClient", url = "${endpoint.delivery-service}")
public interface DeliveryServiceClient {
  @PostMapping("/delivery/status")
  SuccessResponse<DeliveryStatusResponseDto> getDeliveryStatus(@RequestBody List<Long> fundingId);
}
