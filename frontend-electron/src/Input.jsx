import "./App.css";

export default function Input({
  label,
  name,
  value,
  inputRef,
  onChange,
  onFocus,
  onKeyDown,
}) {
  return (
    <div>
      <div className="flex items-center justify-between">
        <label
          htmlFor={label.toLowerCase()}
          className="block text-sm font-medium leading-6 text-gray-900"
        >
          {name}
        </label>
      </div>
      <div className="mt-2">
        <input
          id={label.toLowerCase()}
          name={label.toLowerCase()}
          value={value}
          ref={inputRef}
          type="number"
          onChange={onChange}
          onFocus={onFocus}
          onKeyDown={onKeyDown}
          required
          min={0}
          className="pl-1 block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6 bg-amber-50"
        />
      </div>
    </div>
  );
}
