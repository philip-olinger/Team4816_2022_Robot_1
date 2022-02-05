/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * Import the libraries and classes you will use here
 */
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.CANSparkMaxLowLevel.MotorType.*;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends TimedRobot {
  /**
   * Declare all your objects here
   *
   * SpeedControllers (CANSparkMax)
   * SpeedControllerGroup
   * DifferentialDrive
   * Joystick (or other controller types)
   * Compressor (for pneumatics)
   * Solenoids (pneumatic pistons)
   */
  private CANSparkMax m_leftFrontMotor, m_leftRearMotor;
  private CANSparkMax m_rightFrontMotor, m_rightRearMotor;
  private SpeedControllerGroup m_leftMotors, m_rightMotors;
  private DifferentialDrive m_robotDrive;
  private Compressor m_compressor; 
  private XboxController m_stick;
  private final Timer m_timer = new Timer();

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    /**
     * Instantiate your objects from above and assign them here
     * 
     * CAN Bus device IDS: 
     * https://docs.wpilib.org/en/latest/docs/software/can-devices/can-addressing.html
     */
    
    
    m_rightFrontMotor = new CANSparkMax(3, CANSparkMaxLowLevel.MotorType.kBrushless);
    m_rightRearMotor = new CANSparkMax(2, CANSparkMaxLowLevel.MotorType.kBrushless);
    // m_rightMiscMotor = new CANSparkMax(1, CANSparkMaxLowLevel.MotorType.kBrushless)
    m_leftRearMotor = new CANSparkMax(5, CANSparkMaxLowLevel.MotorType.kBrushless);
    m_leftFrontMotor = new CANSparkMax(6, CANSparkMaxLowLevel.MotorType.kBrushless);
    // m_rightMiscMotor = new CANSparkMax(4, CANSparkMaxLowLevel.MotorType.kBrushless)

    m_compressor = new Compressor();
    m_stick = new XboxController(0);

    m_leftMotors = new SpeedControllerGroup(m_leftFrontMotor, m_leftRearMotor);
    m_rightMotors = new SpeedControllerGroup(m_rightFrontMotor, m_rightRearMotor);
    m_robotDrive = new DifferentialDrive(m_leftMotors, m_rightMotors);
  }
  /**
   * This function is run once each time the robot enters autonomous mode.
   */
  @Override
  public void autonomousInit() {
    m_timer.reset();
    m_timer.start();
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    // Drive for 2 seconds
    if (m_timer.get() < 3.0) {
      /**
       * At first, we can operate individual motors for 2 seconds here for debugging
       * Later, we will operate the robotDrive subclass to drive the robot
       */
      // m_leftMotors.set(0.5);
      // m_rightMotors.set(0.5);

      m_robotDrive.arcadeDrive(-0.5, 0.0); // drive forwards half speed

    } else {
      m_robotDrive.stopMotor(); // stop robot
      // m_leftMotors.stopMotor();
      // m_rightMotors.stopMotor();
    }
  }

  /**
   * This function is called once each time the robot enters teleoperated mode.
   */
  @Override
  public void teleopInit() {
  }

  /**
   * This function is called periodically during teleoperated mode.
   */
  @Override
  public void teleopPeriodic() {
    // m_robotDrive.arcadeDrive(-m_stick.getY(Hand.kLeft), m_stick.getX(Hand.kLeft));
    
    // For tank drive, we need two sticks
    
    double y_val = m_stick.getRightY();
    double x_val = m_stick.getLeftY();
    // Keep the sign for later on
    double y_sign = -1.0;
    double x_sign = -1.0;
    if(y_val < 0){
      y_sign = -y_sign;
    }
    if(x_val < 0){
      x_sign = -x_sign;
    }
    // Square the inputs for exponential response, but keep the sign
    m_robotDrive.tankDrive((y_sign * (y_val * y_val)),
                           (x_sign * (x_val * x_val)));
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
