package com.ponsak.autoandgeneral.demo.controller;

import com.ponsak.autoandgeneral.demo.model.Todo;
import com.ponsak.autoandgeneral.demo.repository.TodoRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

@RestController
@RequestMapping({"/"})
public class TodoController {

    private TodoRepository repository;

    TodoController(TodoRepository contactRepository) {
        this.repository = contactRepository;
    }

    /**
    *Tasks Validate Brackets
    */
    @GetMapping(path = {"/tasks/validateBrackets"})
    public ResponseEntity brackets(@RequestParam("input") String input){
    	//Validate input length
		if(input.length() > 100) {
			HashMap<String, Object> rtn = getBracketsErrJson(input);
			return ResponseEntity.status(400).body(rtn);
		}
		
    	boolean isBalanced = validateBrackets(input);
		HashMap<String, Object> rtnMap = new HashMap<>();
		rtnMap.put("input", input);
		rtnMap.put("isBalanced", (isBalanced));
		return ResponseEntity.ok().body(rtnMap);
    }
    
    /**
    *Find All Todo
    */
    @GetMapping(path = {"/todo"})
    public List findAll(){
    	//Return All Todo
        return repository.findAll();
    }
    
    /**
    *Find Todo By Id
    */
    @GetMapping(path = {"/todo/{id}"})
    public ResponseEntity findById(@PathVariable long id){
    	//Check if exist
    	if(!repository.existsById(id)) {
    		return ResponseEntity.status(404).body(getNotFoundJson(id));
    	}
    	//Return Todo
    	else {
    		return repository.findById(id).map(todo -> ResponseEntity.ok().body(todo)).get();
    	}
    }
    
    /**
    *Create Todo
    */
	@PostMapping(path = {"/todo"})
	public ResponseEntity createOrSaveTodo(@RequestBody Todo todo) {
		//Validate Text
		if(todo.getText().length() < 1 || todo.getText().length() > 50) {
			HashMap<String, Object> rtn = getErrJson(todo);
			return ResponseEntity.status(400).body(rtn);
		}
		//Add Todo
		else {
			todo.setCreatedAt(getDateStr());
			return ResponseEntity.ok().body(repository.save(todo));
		}
	}
	
	/**
	*Update Todo
	*/
	@PatchMapping(path = {"/todo/{id}"})
    public ResponseEntity updateTodo(@RequestBody Todo newTodo, @PathVariable long id){
		//Check if exist
    	if(!repository.existsById(id)) {
    		return ResponseEntity.status(404).body(getNotFoundJson(id));
    	}
    	//Validate Text
    	else if((newTodo.getText() != null && newTodo.getText().length() < 1 )|| (newTodo.getText() != null && newTodo.getText().length() > 50)) {
			HashMap<String, Object> rtn = getErrJson(newTodo);
			return ResponseEntity.status(400).body(rtn);
		}
    	//Update Todo
    	else {
	        return repository.findById(id).map(todo -> {
	        	if(newTodo.getText() != null){
	        		todo.setText(newTodo.getText());
	        	}
	        	todo.setIsCompleted(newTodo.getIsCompleted());
				return ResponseEntity.ok().body(repository.save(todo));
			}).get();
    	}
    }
	
	
	private boolean validateBrackets(String brackets) {
        boolean result = true;
        Stack<Character> stack = new Stack<Character>();
        char current, previous;
        for(int i = 0; i < brackets.length(); i++) {
            current = brackets.charAt(i);
            if(current == '(' || current == '[' || current == '{') {
                stack.push(current);
            } else if(current == ')' || current == ']' || current == '}') {
                if(stack.isEmpty()) {
                    result = false;
                } else {
                    previous = stack.peek();
                    if((current == ')' && previous == '(') || (current == ']' && previous == '[') || (current == '}' && previous == '{')) {
                        stack.pop();
                    } else {
                        result = false;
                    }
                }
            }
        }
        if(!stack.isEmpty()) {
            result = false;
        }
        return result;
    }
	
	private HashMap<String, Object> getBracketsErrJson(String input){
		HashMap<String, Object> rtn = new HashMap<>();
		
		HashMap<String, String> errMap = new HashMap<>();
		errMap.put("location", "params");
		errMap.put("param", "input");
		errMap.put("msg", "Must be less than 100 chars long");
		errMap.put("value", input);
	    
	    List<HashMap> errList = new ArrayList<HashMap>();
	    errList.add(errMap);
	    
	    rtn.put("details", errList);
	    rtn.put("name", "ValidationError");
	    return rtn;
	}
	
	private HashMap<String, Object> getErrJson(Todo todo){
		HashMap<String, Object> rtn = new HashMap<>();
		
		HashMap<String, String> errMap = new HashMap<>();
		errMap.put("location", "params");
		errMap.put("param", "text");
		errMap.put("msg", "Must be between 1 and 50 chars long");
		errMap.put("value", todo.getText());
	    
	    List<HashMap> errList = new ArrayList<HashMap>();
	    errList.add(errMap);
	    
	    rtn.put("details", errList);
	    rtn.put("name", "ValidationError");
	    return rtn;
	}
	
	private HashMap<String, Object> getNotFoundJson(long id){
		HashMap<String, Object> rtn = new HashMap<>();
		
		HashMap<String, String> errMap = new HashMap<>();
		errMap.put("message", "Item with " + id + " not found");
	    
	    List<HashMap> errList = new ArrayList<HashMap>();
	    errList.add(errMap);
	    
	    rtn.put("details", errList);
	    rtn.put("name", "NotFoundError");
	    return rtn;
	}
	
	private String getDateStr(){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		String date = simpleDateFormat.format(new Date()).replaceFirst(" ", "T") + "Z";
		return date;
	}
}
