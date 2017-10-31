package main;


import java.nio.file.Files;


import org.apache.bcel.Constants;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantString;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.BytecodeScanningDetector;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.classfile.ClassDescriptor;
import edu.umd.cs.findbugs.classfile.DescriptorFactory;
import edu.umd.cs.findbugs.classfile.FieldDescriptor;
import edu.umd.cs.findbugs.classfile.MethodDescriptor;

public class FilesLinesDetector extends BytecodeScanningDetector

{
    String fieldName = "";
    private static final ClassDescriptor JAVA_NIO_FILES =
            DescriptorFactory.createClassDescriptor(Files.class);
    final BugReporter bugReporter;

    public FilesLinesDetector(final BugReporter bugReporter)
    {
        this.bugReporter = bugReporter;
    }

    public void sawMethod()
    {
        final MethodDescriptor invokedMethod = getMethodDescriptorOperand();
        // Cheking of method starting with s
        if(invokedMethod != null && invokedMethod.getName().charAt(0) < 'a' || invokedMethod.getName().charAt(0) > 'z'){
            BugInstance bug = new BugInstance(this,
                    "METHOD_STARTING_WITH_SMALL",
                    HIGH_PRIORITY)
                    .addClassAndMethod(this)
                    .addSourceLine(this);
            bugReporter.reportBug(bug);
        }

        if(this.getMethodName().charAt(0) < 'a' || this.getMethodName().charAt(0) > 'z'){
            BugInstance bug = new BugInstance(this,
                    "METHOD_STARTING_WITH_SMALL",
                    HIGH_PRIORITY)
                    .addClassAndMethod(this)
                    .addSourceLine(this);
            bugReporter.reportBug(bug);
        }
    }

    public void sawField()
    {
        final FieldDescriptor fieldDescriptor = getFieldDescriptor();
        if(fieldDescriptor != null &&
                (fieldDescriptor.getName().contains("id")))
        {
            bugReporter.reportBug(new BugInstance(
                            this,
                            "FILES_LINES_CALLED",
                            HIGH_PRIORITY)
                            .addClassAndMethod(this)
                            .addSourceLine(this)
            );
        }


        // if the field type is double throw a bug
        if(fieldDescriptor != null &&
                (fieldDescriptor.getSignature().equals("Double")))
        {
            bugReporter.reportBug(new BugInstance(
                            this,
                            "DOUBLE_DATA_TYPE_USED",
                            HIGH_PRIORITY)
                            .addClassAndMethod(this)
                            .addSourceLine(this)
            );
        }
        fieldName = this.getFieldName();
        System.out.println("FN Value :"+this.getFieldName());
    }

    public void sawDouble(double seen)
    {
        bugReporter.reportBug(new BugInstance(
                        this,
                        "DOUBLE_DATA_TYPE_USED",
                        HIGH_PRIORITY)
                        .addClassAndMethod(this)
                        .addSourceLine(this)
        );


    }

    public void sawOpcode(int seen) {


        if (seen != Constants.LDC
                && seen != Constants.LDC_W) {
            // If this bytecode is not a load constant,
            // we're not interested.
            return;
        }

        Constant operand = this.getConstantRefOperand();
        if (!(operand instanceof ConstantString)) {
            // If the constant being loaded is not a String,
            // we're not interested.
            return;
        }

        String value = ((ConstantString)operand).getBytes(this.getConstantPool());

        if (!value.contains("/partition")) {
            // The String literal being loaded is forbidden!
            // Report Bug.
            bugReporter.reportBug(new BugInstance(
                            this,
                            "ID_FIELD_WITHOUT_PARTITION",
                            HIGH_PRIORITY)
                            .addClassAndMethod(this)
                            .addSourceLine(this)
            );
        }

    }

}
