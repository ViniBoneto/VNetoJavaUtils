package com.vneto.java.utils.cmd;

import java.io.File;
import java.io.IOException;
import java.util.Map;


public class CmdExec {

	public static int exec(String[] cmdarray, String[] envp, File dir) throws IOException, InterruptedException {
		Process p = Runtime.getRuntime().exec(cmdarray, envp, dir);
		
		do {
			p.waitFor();
		} while(p.isAlive()) ;
		
		return p.exitValue();
	}

	public static int exec(String[] cmdarray, String[] envp) throws IOException, InterruptedException {
		return exec(cmdarray, envp, null);
	}
	
	public static int exec(String... cmd) throws IOException, InterruptedException {
		return exec(cmd, null, null);
	}
	
	public static Process execAssync(String[] cmdarray, String[] envp, File dir) throws IOException {
		return Runtime.getRuntime().exec(cmdarray, envp, dir);
	}

	public static Process execAssync(String[] cmdarray, String[] envp) throws IOException {
		return execAssync(cmdarray, envp, null);
	}
	
	public static Process execAssync(String... cmd) throws IOException {
		return execAssync(cmd, null, null);
	}
	
	public static int exec(String[] cmdarray, String[] envp, File dir, boolean redirectErrStream, boolean inheritIO) throws IOException, InterruptedException {
		Integer _int = (Integer) exec(cmdarray, envp, dir, true, redirectErrStream, false, inheritIO, null, null, null);
		
		return _int.intValue();
	}

	public static int exec(String[] cmdarray, String[] envp, File dir, boolean redirectErrStream, boolean append, boolean inheritIO, 
			Object stdIn, Object stdOut, Object stdErr) throws IOException, InterruptedException {
		Integer _int = (Integer) exec(cmdarray, envp, dir, true, redirectErrStream, append, inheritIO, stdIn, stdOut, stdErr);
		
		return _int.intValue();
	}

	public static Process execAssync(String[] cmdarray, String[] envp, File dir, boolean redirectErrStream, boolean inheritIO) throws IOException, InterruptedException {
		return (Process) exec(cmdarray, envp, dir, false, redirectErrStream, false, inheritIO, null, null, null);
	}
	
	public static Process execAssync(String[] cmdarray, String[] envp, File dir, boolean redirectErrStream, boolean append, boolean inheritIO, 
			Object stdIn, Object stdOut, Object stdErr) throws IOException, InterruptedException {
		return (Process) exec(cmdarray, envp, dir, false, redirectErrStream, append, inheritIO, stdIn, stdOut, stdErr);
	}
	
	private static Object exec(String[] cmdarray, String[] envp, File dir, boolean sync, boolean redirectErrStream, boolean append, boolean inheritIO, 
			Object stdIn, Object stdOut, Object stdErr) throws IOException, InterruptedException {
		ProcessBuilder.Redirect in;
		ProcessBuilder.Redirect out;
		ProcessBuilder.Redirect err;
		ProcessBuilder pb;
		
		if(cmdarray == null || cmdarray.length == 0)
			throw new IllegalArgumentException("Parâmetro cmdarray (contendo comando a executar e argumentos opicionais) é inválido. Favor verificar!");

		pb = new ProcessBuilder(cmdarray); 

		if(inheritIO) {
			pb.inheritIO();
		}
		else {
		
			if(stdIn != null) {
				in = verifyProcInStream(stdIn);
				pb.redirectInput(in);
			}
			
			if(stdOut != null) {
				out = verifyProcOutStream(stdOut, append);
				pb.redirectOutput(out);
			}
			
			err = (!redirectErrStream && stdErr != null) ? verifyProcOutStream(stdErr, append) : null;
			
			if(err != null)
				pb.redirectError(err);
				
			pb.redirectErrorStream(redirectErrStream);
		}
		
		if(envp != null && envp.length > 0) {
			Map<String, String> env = pb.environment();
			String entry[];
			
			for (String envEntry : envp) {
				entry = envEntry.split("=");
				
				if(entry.length != 2)
					throw new IllegalArgumentException("Variáveis de ambiente (parâmetro envp) passadas numa formatação inválida. Favor verificar");
					
				env.put(entry[0], entry[1]);
			}
		}
		
		if(dir != null) {
			if(!dir.isDirectory())
				throw new IllegalArgumentException("Parâmetro dir passa um diretório de trabalho inválido. Favor verificar");
			
			pb.directory(dir);
		}

		Process p = pb.start();
		
		if(!sync)
			return p;
		
		do {
			p.waitFor();
		} while(p.isAlive()) ;
		
		return new Integer(p.exitValue());

	}

	private static ProcessBuilder.Redirect verifyProcOutStream(Object stdOut, boolean append) {
		if(stdOut != null) {
			if( stdOut instanceof ProcessBuilder.Redirect )
				return (ProcessBuilder.Redirect) stdOut;
			
			if(stdOut instanceof File) {
				return (append) ? ProcessBuilder.Redirect.appendTo((File)stdOut) : ProcessBuilder.Redirect.to((File)stdOut);
			}
		}
		
		return null;
	}

	private static ProcessBuilder.Redirect verifyProcInStream(Object stdIn) {
		if(stdIn != null) {
			if( stdIn instanceof ProcessBuilder.Redirect )
				return (ProcessBuilder.Redirect) stdIn;
			
			if(stdIn instanceof File) {
				return ProcessBuilder.Redirect.from((File)stdIn);
			}
		}

		return null;
	}	
}
