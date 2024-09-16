package com.esharkycorp.distributed_flow.operations;

import com.esharkycorp.distributed_flow.models.FileDeliveryOrder;
import com.hilabs.cdi.common.queuebuilder.abstracts.AbstractConsumerResponse;
import com.hilabs.cdi.common.queuebuilder.abstracts.AbstractDataHandler;
import com.hilabs.cdi.common.queuebuilder.abstracts.FatalException;
import com.hilabs.cdi.common.queuebuilder.abstracts.RetryException;
import com.hilabs.cdi.common.queuebuilder.abstracts.WarningException;

public class DeliveryFileOperation extends AbstractDataHandler<FileDeliveryOrder> {
    @Override
    public AbstractConsumerResponse process(FileDeliveryOrder data) throws RetryException, FatalException, WarningException {
        return null;
    }
}
