package com.mrudul.tourandtravel.driver.driver_interfaces;

import com.mrudul.tourandtravel.driver.driver_models.DriverRequestModel;

/**
 * Callback interface for Accept / Reject actions on driver request cards.
 */
public interface DriverRequestClickListener {
    void onAcceptRequest(DriverRequestModel model, int position);
    void onRejectRequest(DriverRequestModel model, int position);
}