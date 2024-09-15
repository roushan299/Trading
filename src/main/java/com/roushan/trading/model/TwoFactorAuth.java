package com.roushan.trading.model;

import com.roushan.trading.domain.VERIFICATION_TYPE;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TwoFactorAuth {

    private boolean isEnabled = false;
    private VERIFICATION_TYPE sendTo;


}
