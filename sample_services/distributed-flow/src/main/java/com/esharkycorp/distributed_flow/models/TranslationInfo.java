package com.esharkycorp.distributed_flow.models;

import com.hilabs.cdi.common.queuebuilder.abstracts.AbstractMessageQueuePayload;
import com.hilabs.cdi.common.queuebuilder.annotations.AbstractPayload;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AbstractPayload
public class TranslationInfo extends AbstractMessageQueuePayload {
    String fileLocation;
    String translationType;
    Integer operationTimes;
}
