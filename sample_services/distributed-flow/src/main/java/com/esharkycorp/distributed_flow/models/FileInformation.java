package com.esharkycorp.distributed_flow.models;

import com.hilabs.cdi.common.queuebuilder.abstracts.AbstractMessageQueuePayload;
import com.hilabs.cdi.common.queuebuilder.annotations.AbstractPayload;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AbstractPayload
public class FileInformation extends AbstractMessageQueuePayload {
}
