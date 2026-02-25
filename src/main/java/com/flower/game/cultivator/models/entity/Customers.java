package com.flower.game.cultivator.models.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 用户集合实体类
 *
 * @author F1ower
 * @since 2026-2-24
 */
@Data
public class Customers implements Serializable {

    @Serial
    private static final long serialVersionUID = -1433009673155924723L;

    /**
     * 用户集合
     */
    private List<Customer> customers;
}
