package com.lilamaris.cozyr.reservation.jpa.support;

import com.lilamaris.cozyr.reservation.application.port.out.RoomPolicyStore;
import com.lilamaris.cozyr.reservation.application.port.out.status.RoomUpdateStatus;
import com.lilamaris.cozyr.reservation.domain.RoomId;
import com.lilamaris.cozyr.reservation.domain.RoomOpPolicy;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ControlledRoomOpPolicyStore implements RoomPolicyStore {
    public enum LockOrder { SHARE_FIRST, UPDATE_FIRST }

    private final RoomPolicyStore delegate;
    private LockOrder order;
    private CyclicBarrier barrier;

    public ControlledRoomOpPolicyStore(RoomPolicyStore delegate) {
        this.delegate = delegate;
    }

    // Call before submitting workers, and only after previous workers have terminated.
    public void prepare(LockOrder order) {
        this.order = Objects.requireNonNull(order);
        this.barrier = new CyclicBarrier(2);
    }

    @Override
    public Optional<RoomOpPolicy> findForUpdate(RoomId roomId) {
        requireTransaction();
        if (order == LockOrder.SHARE_FIRST) awaitBarrier();
        var policy = delegate.findForUpdate(roomId);
        if (order == LockOrder.UPDATE_FIRST) awaitBarrier();
        return policy;
    }

    @Override
    public Optional<RoomOpPolicy> findForShare(RoomId roomId) {
        requireTransaction();
        if (order == LockOrder.UPDATE_FIRST) awaitBarrier();
        var policy = delegate.findForShare(roomId);
        if (order == LockOrder.SHARE_FIRST) awaitBarrier();
        return policy;
    }

    private void requireTransaction() {
        if (barrier == null || !TransactionSynchronizationManager.isActualTransactionActive())
            throw new IllegalStateException("Prepare the lock order and invoke within a transaction");
    }

    private void awaitBarrier() {
        try {
            // The leader holds its DB lock; the follower has not requested its lock yet.
            barrier.await(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted waiting for the competing transaction", e);
        } catch (BrokenBarrierException | TimeoutException e) {
            throw new IllegalStateException("Both transactions must reach the policy lock boundary", e);
        }
    }

    @Override
    public void save(RoomOpPolicy policy) {
        delegate.save(policy);
    }

    @Override
    public RoomUpdateStatus updateDeactivatedAtWithAwareByOwner(RoomId roomId, UUID userId, Instant deactivatedAt, Instant updatedAt) {
        return delegate.updateDeactivatedAtWithAwareByOwner(roomId, userId, deactivatedAt, updatedAt);
    }

    @Override
    public RoomUpdateStatus updateActivatedAtWithAwareByOwner(RoomId roomId, UUID userId, Instant activatedAt, Instant updatedAt) {
        return delegate.updateActivatedAtWithAwareByOwner(roomId, userId, activatedAt, updatedAt);
    }
}
