package com.boco.test;

import java.util.List;

import com.boco.concurrent.Executer;

public class Test {
	public static void main(String[] args) {
		Executer e = new Executer(5);
		e.fork(new MyJob<List<Result>>());
		e.fork(new MyJob<Result>());
		List<Result> l = e.join();
	}
}
