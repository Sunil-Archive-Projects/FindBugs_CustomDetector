package main;


import java.nio.file.Files;

import org.apache.bcel.classfile.JavaClass;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.BytecodeScanningDetector;
import edu.umd.cs.findbugs.Priorities;
import edu.umd.cs.findbugs.ba.ClassContext;
import edu.umd.cs.findbugs.classfile.ClassDescriptor;
import edu.umd.cs.findbugs.classfile.DescriptorFactory;
import edu.umd.cs.findbugs.classfile.MethodDescriptor;

public class FilesLinesDetector extends BytecodeScanningDetector
{
    private static final ClassDescriptor JAVA_NIO_FILES =
        DescriptorFactory.createClassDescriptor(Files.class);
    final BugReporter bugReporter;

    public FilesLinesDetector(final BugReporter bugReporter)
    {
        this.bugReporter = bugReporter;
    }
    
//    @Override
//    public void sawMethod()
//    {
//        final MethodDescriptor invokedMethod 
//            = getMethodDescriptorOperand();
//        final ClassDescriptor invokedObject 
//            = getClassDescriptorOperand();
//        if(invokedMethod != null && 
//           "lines".equals(invokedMethod.getName()) && 
//           JAVA_NIO_FILES.equals(invokedObject))
//        {
//            bugReporter.reportBug(
//                 new BugInstance(this, 
//                                 "FILES_LINES_CALLED", 
//                                 HIGH_PRIORITY)
//                     .addClassAndMethod(this)
//                     .addSourceLine(this)
//            );
//        }
//    }
    
    @Override
	public void visitClassContext(ClassContext classContext) {
		// TODO Auto-generated method stub
	 JavaClass cls = classContext.getJavaClass();
	 String clsname = cls.getClassName();
	 
	 if(clsname.charAt(0) != '1')
	 {
		 BugInstance bug = new BugInstance(this, 
               "FILES_LINES_CALLED", 
               HIGH_PRIORITY)
   .addClassAndMethod(this)
   .addSourceLine(this);
		 bugReporter.reportBug(bug);
	 }
 
	}
}