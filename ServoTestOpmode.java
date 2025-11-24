package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * FTC 舵机测试程序（Driver Hub 专用）
 * 支持自动往复摆动 + 手柄按钮控制
 * 接线：舵机接 REV Hub 的 S1 端口（可修改下面的 SERVO_PORT 变量更换端口）
 */
@TeleOp(name = "Servo Test (Auto + Manual)", group = "Test")
public class ServoTestOpMode extends LinearOpMode {

    // 1. 配置参数（根据实际硬件修改）
    private static final String SERVO_PORT = "servo1";  // 舵机接 Hub 的端口名（需在 Driver Hub 中匹配）
    private static final double MIN_ANGLE = 0.0;       // 舵机最小角度（0°）
    private static final double MAX_ANGLE = 180.0;     // 舵机最大角度（180°）
    private static final long DELAY_MS = 20;           // 角度切换延时（毫秒，越小转动越快）
    private static final long STOP_DELAY_MS = 1000;    // 端点停留时间（毫秒）

    // 舵机对象
    private Servo myServo;

    @Override
    public void runOpMode() throws InterruptedException {
        // 2. 初始化硬件
        initHardware();

        // 3. 等待 Driver Hub 启动（显示初始化完成）
        telemetry.addData("状态", "初始化完成，等待启动...");
        telemetry.addData("操作说明", "A=0° | B=90° | X=180° | Y=45° | 自动模式：启动后自动摆动");
        telemetry.update();

        // 等待 Driver Hub 上的 "START" 按钮被按下
        waitForStart();

        // 4. 运行模式选择（根据手柄是否操作切换，默认自动模式）
        boolean isAutoMode = true;  // 初始为自动模式

        while (opModeIsActive()) {  // 程序运行中（未按 STOP 按钮）
            // 检测手柄按钮，切换到手动控制
            handleGamepadInputs();

            // 自动模式：往复摆动
            if (isAutoMode && !gamepad1.a && !gamepad1.b && !gamepad1.x && !gamepad1.y) {
                autoSwing();
            }

            // 实时显示舵机状态
            updateTelemetry();
        }
    }

    /**
     * 初始化舵机硬件（必须与 Driver Hub 中的配置名一致）
     */
    private void initHardware() {
        // 从硬件映射中获取舵机（"servo1" 需在 Driver Hub 中配置）
        myServo = hardwareMap.get(Servo.class, SERVO_PORT);

        // 配置舵机角度范围（FTC Servo 类默认 0~1，但可通过以下方法映射为实际角度）
        myServo.scaleRange(MIN_ANGLE, MAX_ANGLE);  // 将舵机的 0~1 映射为 0°~180°

        // 初始角度设为中间位置（90°）
        myServo.setPosition(90.0);
    }

    /**
     * 自动模式：舵机从 MIN_ANGLE 转到 MAX_ANGLE，再转回 MIN_ANGLE
     */
    private void autoSwing() {
        // 从 MIN_ANGLE 转到 MAX_ANGLE（每次增加 1°）
        for (double angle = MIN_ANGLE; angle <= MAX_ANGLE && opModeIsActive(); angle++) {
            myServo.setPosition(angle);
            telemetry.addData("模式", "自动摆动");
            telemetry.addData("当前角度", "%.0f°", angle);
            telemetry.update();
            sleep(DELAY_MS);  // 延时，让舵机平稳转动
        }

        sleep(STOP_DELAY_MS);  // 180° 位置停留 1 秒

        // 从 MAX_ANGLE 转回 MIN_ANGLE（每次减少 1°）
        for (double angle = MAX_ANGLE; angle >= MIN_ANGLE && opModeIsActive(); angle--) {
            myServo.setPosition(angle);
            telemetry.addData("模式", "自动摆动");
            telemetry.addData("当前角度", "%.0f°", angle);
            telemetry.update();
            sleep(DELAY_MS);
        }

        sleep(STOP_DELAY_MS);  // 0° 位置停留 1 秒
    }

    /**
     * 手动模式：通过手柄按钮控制舵机角度
     */
    private void handleGamepadInputs() {
        // A 键：转到 0°
        if (gamepad1.a) {
            myServo.setPosition(0.0);
            telemetry.addData("模式", "手动控制（A键）");
        }
        // B 键：转到 90°（中间位置）
        else if (gamepad1.b) {
            myServo.setPosition(90.0);
            telemetry.addData("模式", "手动控制（B键）");
        }
        // X 键：转到 180°
        else if (gamepad1.x) {
            myServo.setPosition(180.0);
            telemetry.addData("模式", "手动控制（X键）");
        }
        // Y 键：转到 45°
        else if (gamepad1.y) {
            myServo.setPosition(45.0);
            telemetry.addData("模式", "手动控制（Y键）");
        }
    }

    /**
     * 更新 Driver Hub 屏幕显示信息
     */
    private void updateTelemetry() {
        telemetry.addData("当前角度", "%.0f°", myServo.getPosition());
        telemetry.addData("舵机端口", SERVO_PORT);
        telemetry.addData("操作说明", "A=0° | B=90° | X=180° | Y=45°");
        telemetry.update();  // 刷新屏幕显示
    }
}
