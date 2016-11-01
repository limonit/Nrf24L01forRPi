package pl.elektrofanklub.nrf24l01;


/**
 * Created by trojan on 10.10.16.
 */
final class Nrf24L01Registers {

    /* nRF24L01 Instruction Definitions */
    public static final short WRITE_REG = 0x20;
    /**
     * < Register write command
     */
    public static final short R_RX_PL_WID = 0x60;
    /**
     * < Read RX width payload command
     */
    public static final short R_RX_PAYLOAD = 0x61;
    /**
     * < Read RX payload command
     */
    public static final short W_TX_PAYLOADD = 0xA0;
    /**
     * < Write TX payload command
     */
    public static final short W_ACK_PAYLOAD = 0xA8;
    /**
     * < Write ACK payload command
     */
    public static final short WR_NAC_TX_PLOAD = 0xB0;
    /**
     * < Write ACK payload command
     */
    public static final short FLUSH_TX = 0xE1;
    /**
     * < Flush TX register command
     */
    public static final short FLUSH_RX = 0xE2;
    /**
     * < Flush RX register command
     */
    public static final short REUSE_TX_PL = 0xE3;
    /**
     * < Reuse TX payload command
     */
    public static final short LOCK_UNLOCK = 0x50;
    /**
     * < Lock/unlcok exclusive features
     */

    public static final short NOP = 0xFF;  /**< No Operation command, used for reading status register */
//@}

    /**
     * @name - Register Memory Map -
     */
//@{
/* nRF24L01 * Register Definitions * */
    public static final short CONFIG = 0x00;
    /**
     * < nRF24L01 config register
     */
    public static final short EN_AA = 0x01;
    /**
     * < nRF24L01 enable Auto-Acknowledge register
     */
    public static final short EN_RXADDR = 0x02;
    /**
     * < nRF24L01 enable RX addresses register
     */
    public static final short SETUP_AW = 0x03;
    /**
     * < nRF24L01 setup of address width register
     */
    public static final short SETUP_RETR = 0x04;
    /**
     * < nRF24L01 setup of automatic retransmission register
     */
    public static final short RF_CH = 0x05;
    /**
     * < nRF24L01 RF channel register
     */
    public static final short RF_SETUP = 0x06;
    /**
     * < nRF24L01 RF setup register
     */
    public static final short STATUS = 0x07;
    /**
     * < nRF24L01 status register
     */
    public static final short OBSERVE_TX = 0x08;
    /**
     * < nRF24L01 transmit observe register
     */
    public static final short CD = 0x09;
    /**
     * < nRF24L01 carrier detect register
     */
    public static final short RX_ADDR_P0 = 0x0A;
    /**
     * < nRF24L01 receive address data pipe0
     */
    public static final short RX_ADDR_P1 = 0x0B;
    /**
     * < nRF24L01 receive address data pipe1
     */
    public static final short RX_ADDR_P2 = 0x0C;
    /**
     * < nRF24L01 receive address data pipe2
     */
    public static final short RX_ADDR_P3 = 0x0D;
    /**
     * < nRF24L01 receive address data pipe3
     */
    public static final short RX_ADDR_P4 = 0x0E;
    /**
     * < nRF24L01 receive address data pipe4
     */
    public static final short RX_ADDR_P5 = 0x0F;
    /**
     * < nRF24L01 receive address data pipe5
     */
    public static final short TX_ADDR = 0x10;
    /**
     * < nRF24L01 transmit address
     */
    public static final short RX_PW_P0 = 0x11;
    /**
     * < nRF24L01 \# of shorts in rx payload for pipe0
     */
    public static final short RX_PW_P1 = 0x12;
    /**
     * < nRF24L01 \# of shorts in rx payload for pipe1
     */
    public static final short RX_PW_P2 = 0x13;
    /**
     * < nRF24L01 \# of shorts in rx payload for pipe2
     */
    public static final short RX_PW_P3 = 0x14;
    /**
     * < nRF24L01 \# of shorts in rx payload for pipe3
     */
    public static final short RX_PW_P4 = 0x15;
    /**
     * < nRF24L01 \# of shorts in rx payload for pipe4
     */
    public static final short RX_PW_P5 = 0x16;
    /**
     * < nRF24L01 \# of shorts in rx payload for pipe5
     */
    public static final short FIFO_STATUS = 0x17;
    /**
     * < nRF24L01 FIFO status register
     */
    public static final short DYNPD = 0x1C;
    /**
     * < nRF24L01 Dynamic payload setup
     */
    public static final short FEATURE = 0x1D; /**< nRF24L01 Exclusive feature setup */

//@}

/* nRF24L01 related definitions */
/* Interrupt definitions */
/* Operation mode definitions */

    /**
     * An enum describing the radio's irq sources.
     */

    public static final short HAL_NRF_MAX_RT = 4;
    /**
     * < Max retries interrupt
     */
    public static final short HAL_NRF_TX_DS = 5;
    /**
     * < TX data sent interrupt
     */
    public static final short HAL_NRF_RX_DR = 6;           /**< RX data received interrupt */


/* Operation mode definitions */
    /**
     * An enum describing the radio's power mode.
     */

    public static final short HAL_NRF_PTX = 0;
    /**
     * < Primary TX operation
     */
    public static final short HAL_NRF_PRX = 1;            /**< Primary RX operation */


    /**
     * An enum describing the radio's power mode.
     */

    public static final short HAL_NRF_PWR_DOWN = 0;
    /**
     * < Device power-down
     */
    public static final short HAL_NRF_PWR_UP = 1;         /**< Device power-up */


    /**
     * An enum describing the radio's output power mode's.
     */

    public static final short HAL_NRF_18DBM = 0;
    /**
     * < Output power set to -18dBm
     */
    public static final short HAL_NRF_12DBM = 1;
    /**
     * < Output power set to -12dBm
     */
    public static final short HAL_NRF_6DBM = 2;
    /**
     * < Output power set to -6dBm
     */
    public static final short HAL_NRF_0DBM = 3;          /**< Output power set to 0dBm   */


    /**
     * An enum describing the radio's on-air datarate.
     */

    public static final short HAL_NRF_1MBPS = 0;
    /**
     * < Datarate set to 1 Mbps
     */
    public static final short HAL_NRF_2MBPS = 1;       /**< Datarate set to 2 Mbps  */


    /**
     * An enum describing the radio's PLL mode.
     */

    public static final short HAL_NRF_PLL_UNLOCK = 0;
    /**
     * < PLL unlocked, normal operation
     */
    public static final short HAL_NRF_PLL_LOCK = 1;       /**< PLL locked, test mode  */


    /**
     * An enum describing the radio's LNA mode.
     */

    public static final short HAL_NRF_LNA_LCURR = 0;
    /**
     * < LNA set to low current mode
     */
    public static final short HAL_NRF_LNA_HCURR = 1;     /**< LNA set to high current mode */


    /**
     * An enum describing the radio's CRC mode.
     */

    public static final short HAL_NRF_CRC_OFF = 0;
    /**
     * < CRC check disabled
     */
    public static final short HAL_NRF_CRC_8BIT = 2;
    /**
     * < CRC check set to 8-bit
     */
    public static final short HAL_NRF_CRC_16BIT = 3;     /**< CRC check set to 16-bit */


    /**
     * An enum describing the read/write payload command.
     */

    public static final short HAL_NRF_TX_PLOAD = 7;
    /**
     * < TX payload definition
     */
    public static final short HAL_NRF_RX_PLOAD = 8;
    /**
     * < RX payload definition
     */
    public static final short HAL_NRF_ACK_PLOAD = 9;
    public static final short HAL_NRF_TX_PLOAD_NOACK = 10;


/** Structure containing the radio's address map.
 * Pipe0 contains 5 unique address shorts,
 * while pipe[1..5] share the 4 MSB shorts, set in pipe1.
 * <p><b> - Remember that the LSB short for all pipes have to be unique! -</b>
 */
// nRF24L01 Address struct

/*
//typedef struct {
//   uint8_t p0[5];            /**< Pipe0 address, 5 shorts */
//    uint8_t p1[5];            /**< Pipe1 address, 5 shorts, 4 MSB shorts shared for pipe1 to pipe5 */
//    uint8_t p2[1];            /**< Pipe2 address, 1 short */
//    uint8_t p3[1];            /**< Pipe3 address, 1 short */
    //   uint8_t p4[1];            /**< Pipe3 address, 1 short */
    //   uint8_t p5[1];            /**< Pipe3 address, 1 short */
    //   uint8_t tx[5];            /**< TX address, 5 short */
//} hal_nrf_l01_addr_map;


    /**
     * An enum describing the nRF24L01 pipe addresses and TX address.
     */

    public static final short HAL_NRF_PIPE0 = 0;
    /**
     * < Select pipe0
     */
    public static final short HAL_NRF_PIPE1 = 1;
    /**
     * < Select pipe1
     */
    public static final short HAL_NRF_PIPE2 = 2;
    /**
     * < Select pipe2
     */
    public static final short HAL_NRF_PIPE3 = 3;
    /**
     * < Select pipe3
     */
    public static final short HAL_NRF_PIPE4 = 4;
    /**
     * < Select pipe4
     */
    public static final short HAL_NRF_PIPE5 = 5;
    /**
     * < Select pipe5
     */
    public static final short HAL_NRF_TX = 6;
    /**
     * < Refer to TX address
     */
    public static final short HAL_NRF_ALL = 0xFF;      /**< Close or open all pipes*/
    /**< @see hal_nrf_set_address @see hal_nrf_get_address
     @see hal_nrf_open_pipe  @see hal_nrf_close_pipe */


    /**
     * An enum describing the radio's address width.
     */

    public static final short HAL_NRF_AW_3shortS = 3;
    /**
     * < Set address width to 3 shorts
     */
    public static final short HAL_NRF_AW_4shortS = 4;
    /**
     * < Set address width to 4 shorts
     */
    public static final short HAL_NRF_AW_5shortS = 5;          /**< Set address width to 5 shorts */


    /**
     * @name CONFIG register bit definitions
     */
//@{

    public static final short MASK_RX_DR = 6;
    /**
     * < CONFIG register bit 6
     */
    public static final short MASK_TX_DS = 5;
    /**
     * < CONFIG register bit 5
     */
    public static final short MASK_MAX_RT = 4;
    /**
     * < CONFIG register bit 4
     */
    public static final short EN_CRC = 3;
    /**
     * < CONFIG register bit 3
     */
    public static final short CRCO = 2;
    /**
     * < CONFIG register bit 2
     */
    public static final short PWR_UP = 1;
    /**
     * < CONFIG register bit 1
     */
    public static final short PRIM_RX = 0;    /**< CONFIG register bit 0 */
//@}

    /**
     * @name RF_SETUP register bit definitions
     */
//@{
    public static final short RF_DR_LOW = 5;
    /**
     * < RF_SETUP register bit 5
     */
    public static final short PLL_LOCK = 4;
    /**
     * < RF_SETUP register bit 4
     */
    public static final short RF_DR = 3;
    /**
     * < RF_SETUP register bit 3
     */
    public static final short RF_PWR1 = 2;
    /**
     * < RF_SETUP register bit 2
     */
    public static final short RF_PWR0 = 1;
    /**
     * < RF_SETUP register bit 1
     */
    public static final short LNA_HCURR = 0;    /**< RF_SETUP register bit 0 */
//@}

/* STATUS 0x07 */
    /**
     * @name STATUS register bit definitions
     */
//@{
    public static final short RX_DR = 6;
    /**
     * < STATUS register bit 6
     */
    public static final short TX_DS = 5;
    /**
     * < STATUS register bit 5
     */
    public static final short MAX_RT = 4;
    /**
     * < STATUS register bit 4
     */
    public static final short TX_FULL = 0;    /**< STATUS register bit 0 */
//@}

/* FIFO_STATUS 0x17 */
    /**
     * @name FIFO_STATUS register bit definitions
     */
//@{
    public static final short TX_REUSE = 6;
    /**
     * < FIFO_STATUS register bit 6
     */
    public static final short TX_FIFO_FULL = 5;
    /**
     * < FIFO_STATUS register bit 5
     */
    public static final short TX_EMPTY = 4;
    /**
     * < FIFO_STATUS register bit 4
     */
    public static final short RX_FULL = 1;
    /**
     * < FIFO_STATUS register bit 1
     */
    public static final short RX_EMPTY = 0;
    /**
     * < FIFO_STATUS register bit 0
     */
//@}

    public static final short EN_DPL = 2;
    public static final short EN_ACK_PAY = 1;
    public static final short EN_DYN_ACK = 0;

    public static final short DPL_P5 = 5;
    public static final short DPL_P4 = 4;
    public static final short DPL_P3 = 3;
    public static final short DPL_P2 = 2;
    public static final short DPL_P1 = 1;
    public static final short DPL_P0 = 0;

    public static final short ARD_250US = 0x00;
    public static final short ARD_500US = 0x10;
    public static final short ARD_750US = 0x20;
    public static final short ARD_1000us = 0x30;
    public static final short ARD_1250US = 0x40;
    public static final short ARD_1500US = 0x50;
    public static final short ARD_1750US = 0x60;
    public static final short ARD_2000US = 0x70;

    public static final short ARC_DISABLE = 0x00;
    public static final short ARC_1 = 0x01;
    public static final short ARC_2 = 0x02;
    public static final short ARC_3 = 0x03;
    public static final short ARC_4 = 0x04;

}

