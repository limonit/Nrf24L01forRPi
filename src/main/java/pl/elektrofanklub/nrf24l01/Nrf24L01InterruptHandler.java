package pl.elektrofanklub.nrf24l01;

/**
 * Created by trojan on 30.10.16.
 */
public interface Nrf24L01InterruptHandler {
    /**
     * MAX_RT interrupt handler
     */
    void maxRT();


    /**
     * TX_DS interrupt handler
     */
    void txDS();


    /**
     * RX_RD interrupt handler
     */
    void rxDR();
}
