package com.example.rest_service.Entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAssets {

    @Id
    @Column
    private String username;

    @Column(nullable = false)
    private BigDecimal usdt = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal btc = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal eth = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal doge = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal xrp = BigDecimal.ZERO;

    @OneToOne
    @JoinColumn(name = "joined_username", referencedColumnName = "username")
    private User user;
}
