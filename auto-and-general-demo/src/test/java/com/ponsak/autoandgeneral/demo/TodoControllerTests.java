package com.ponsak.autoandgeneral.demo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.ponsak.autoandgeneral.demo.model.Todo;

public class TodoControllerTests extends AbstractTest {
	@Override
	@Before
	public void setUp() {
		super.setUp();
	}

	@Test
	public void validateBracketsTrue() throws Exception {
		String uri = "/tasks/validateBrackets?input=(([{}]))";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		assertEquals(content, "{\"input\":\"(([{}]))\",\"isBalanced\":true}");
	}

	@Test
	public void validateBracketsFalse() throws Exception {
		String uri = "/tasks/validateBrackets?input=(([{))";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		assertEquals(content, "{\"input\":\"(([{))\",\"isBalanced\":false}");
	}

	@Test
	public void createTodo() throws Exception {
		String uri = "/todo";
		Todo todo = new Todo();
		todo.setId(1l);
		todo.setText("Test Todo");
		todo.setIsCompleted(false);
		String inputJson = super.mapToJson(todo);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	}

	@Test
	public void getTodoById() throws Exception {
		String uri = "/todo/1";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	}

	@Test
	public void updateTodo() throws Exception {
		String uri = "/todo/1";
		Todo todo = new Todo();
		todo.setText("Updated Todo");
		todo.setIsCompleted(true);
		String inputJson = super.mapToJson(todo);
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.patch(uri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	}
}
